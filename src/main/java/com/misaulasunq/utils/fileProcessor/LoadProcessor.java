package com.misaulasunq.utils.fileProcessor;

import com.misaulasunq.exceptions.ClassroomNotFoundException;
import com.misaulasunq.exceptions.DegreeNotFoundException;
import com.misaulasunq.exceptions.DuplicateScheduleException;
import com.misaulasunq.model.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadProcessor {

    private Map<String, Degree> degreesInDbByCode;
    private Map<String, Subject> subjectsByCode;
    private Map<String, Classroom> classroomInDbByNumber;
    private List<Subject> subjectsToUpsert;
    private Map<String, List<RowFileWrapper>> rowsToProcess;

    public LoadProcessor() {
        this.initialize();
    }

    public LoadProcessor(
            Map<String, Degree> degreesMapInDb,
            Map<String, Subject> subjectMapInDb,
            Map<String, Classroom> classroomMapInDb,
            Map<String, List<RowFileWrapper>> rowsToProcess
    ){
        this.initialize();
        this.degreesInDbByCode = degreesMapInDb;
        this.subjectsByCode = subjectMapInDb;
        this.classroomInDbByNumber = classroomMapInDb;
        this.rowsToProcess = rowsToProcess;
    }

    private void initialize(){
        this.subjectsToUpsert = new ArrayList<>();
        this.degreesInDbByCode = new HashMap<>();
        this.subjectsByCode = new HashMap<>();
        this.classroomInDbByNumber = new HashMap<>();
    }

    public void makeRelationships() throws DegreeNotFoundException, ClassroomNotFoundException, DuplicateScheduleException {
        Subject subjectToImport;
        Map<String,Commission> commissionBySubjectCodeAndCommisionName = new HashMap<>();

        Commission currentComission;
        //Iteramos por cada lista de rows de una materia
        for(List<RowFileWrapper> eachRowList: this.rowsToProcess.values()){
            //Creamos u obtenemos el subject del primero de la lista
            subjectToImport = this.getOrCreateSubject(eachRowList.get(0));

            for(RowFileWrapper eachRow : eachRowList) {
                currentComission = this.getOrCreateCommission(
                        commissionBySubjectCodeAndCommisionName,
                        subjectToImport,
                        eachRow
                    );
                commissionBySubjectCodeAndCommisionName.put(
                        subjectToImport.getSubjectCode()+"|"+currentComission.getName(),
                        currentComission
                );
                this.createSchedule(currentComission, eachRow);
            }
            this.subjectsToUpsert.add(subjectToImport);
        }
    }

    private void createSchedule(Commission comission, RowFileWrapper row) throws ClassroomNotFoundException, DuplicateScheduleException {
        if(!this.classroomInDbByNumber.containsKey(row.getClassroom())){
            throw new ClassroomNotFoundException(row.getClassroom());
        }
        Classroom classroom = this.classroomInDbByNumber.get(row.getClassroom());

        if(this.checkIfHaveSameSchedule(comission,row.getDay(),row.getStartTime(),row.getEndTime(),row.getClassroom())){
            throw new DuplicateScheduleException(comission.getSubject().getName(),row.getClassroom(),row.getDay(),row.getStartTime(),row.getEndTime());
        }

        Schedule scheduleToAdd = new Schedule();
        scheduleToAdd.setDay(row.getDay());
        scheduleToAdd.setStartTime(row.getStartTime());
        scheduleToAdd.setEndTime(row.getEndTime());
        scheduleToAdd.setClassroom(classroom);
        scheduleToAdd.setCommission(comission);
        classroom.addSchedule(scheduleToAdd);
        comission.addSchedule(scheduleToAdd);
    }

    private boolean checkIfHaveSameSchedule(Commission comissions, Day day, LocalTime startTime, LocalTime endTime, String classroom){
        for (Schedule existentSchedule : comissions.getSchedules()){
            if( existentSchedule.getClassroom().getNumber().equalsIgnoreCase(classroom)
                && existentSchedule.getDay() == day
                && existentSchedule.getStartTime().equals(startTime)
                && existentSchedule.getEndTime().equals(endTime)){
                return true;
            }
        }
        return false;
    }

    private Commission getOrCreateCommission(Map<String, Commission> commissionMap, Subject subject, RowFileWrapper row) {
        Commission commission;
        String commissionKey = subject.getSubjectCode()+"|"+row.getCommissionName();
        if(commissionMap.containsKey(commissionKey)) {
            commission = commissionMap.get(commissionKey);
        } else {
            commission = this.getOrCreateCommisionFrom(subject,row);

        }
        return commission;
    }

    private Commission getOrCreateCommisionFrom(Subject subject, RowFileWrapper row){
        Commission commission;

        for (Commission com : subject.getCommissions()){
            if(com.getYear().equals(row.getYear())
                && com.getSemester() == row.getSemster()
                && com.getName().equalsIgnoreCase(row.getCommissionName())){
                return com;
            }
        }
        commission = new Commission();
        commission.setName(row.getCommissionName());
        commission.setYear(row.getYear());
        commission.setSemester(row.getSemster());
        commission.setSubject(subject);
        subject.addCommission(commission);
        return commission;
    }

    private Subject getOrCreateSubject(RowFileWrapper row) throws DegreeNotFoundException {
        Subject subject;
        if (this.subjectToImportItsInMap(row)) {
            subject = this.getSubjectMapped(row);
        } else {
            subject = this.createSubject(row);
            this.addDegreeToSubject(subject, row);
        }
        return subject;
    }

    private void addDegreeToSubject(Subject subjectToImport, RowFileWrapper row) throws DegreeNotFoundException {
        if(!this.degreesInDbByCode.containsKey(row.getDegreeCode())){
            throw new DegreeNotFoundException(row.getDegreeCode());
        }
        Degree aDegree = this.degreesInDbByCode.get(row.getDegreeCode());
        subjectToImport.addDegree(aDegree);
        aDegree.addSubject(subjectToImport);
    }

    private Subject createSubject(RowFileWrapper eachRow) {
        Subject subject = new Subject();
        subject.setName(eachRow.getSubjectName());
        subject.setSubjectCode(eachRow.getSubjectCode());

        return subject;
    }

    private boolean subjectToImportItsInMap(RowFileWrapper row) {
        return this.subjectsByCode.containsKey(row.getSubjectCode());
    }

    private Subject getSubjectMapped(RowFileWrapper row) {
        return this.subjectsByCode.get(row.getSubjectCode());
    }

    public List<Subject> getSubjectsToUpsert() {    return subjectsToUpsert;    }
    public void setDegreesInDbByCode(Map<String, Degree> degreesInDbByCode) {   this.degreesInDbByCode = degreesInDbByCode; }
    public void setSubjectsByCode(Map<String, Subject> subjectsByCode) {    this.subjectsByCode = subjectsByCode;   }
    public void setClassroomInDbByNumber(Map<String, Classroom> classroomInDbByNumber) {    this.classroomInDbByNumber = classroomInDbByNumber; }
    public void setRowsToProcess(Map<String, List<RowFileWrapper>> rowsToProcess) { this.rowsToProcess = rowsToProcess; }
}

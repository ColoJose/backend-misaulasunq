package com.misaulasunq.model;

import com.misaulasunq.controller.dto.CommissionDTO;
import com.misaulasunq.controller.dto.ScheduleDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectToParse {

    private String name;
    private String subjectCode;
    private List<CommissionDTO> commissions;
    private Integer degreeId;

    public SubjectToParse() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public List<CommissionDTO> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<CommissionDTO> commissions) {
        this.commissions = commissions;
    }

    public Integer getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Integer degreeId) {
        this.degreeId = degreeId;
    }

    public Subject parse(Degree degreeReceived, List<Classroom> classroomsNewSubject) {
        Subject subject = new Subject();
        subject.setName(this.getName());
        subject.setSubjectCode(this.getSubjectCode());
        subject.setCommissions(this.parseCommissions(subject, classroomsNewSubject));
        // add bidirectional subject and degree
        subject.addDegree(degreeReceived);
        degreeReceived.addSubject(subject);

        return subject;
    }

    private List<Commission> parseCommissions(Subject subject, List<Classroom> classroomsNewSubject) {
        return commissions.stream()
                             .map( com -> this.parseCommission(com,subject, classroomsNewSubject))
                             .collect(Collectors.toList());
    }

    private Commission parseCommission(CommissionDTO commissionDTO, Subject subject, List<Classroom> classroomsNewSubject) {
        Commission commission = new Commission();
        commission.setName(commissionDTO.getName());
        commission.setYear(commissionDTO.getYear());
        commission.setSemester(Semester.valueOf(this.toEnumSemesterString(commissionDTO.getSemester())));
        commission.setSubject(subject);
        commission.setSchedules(this.parseSchedules(commissionDTO.getSchedules(), commission, classroomsNewSubject));

        return commission;
    }



    private List<Schedule> parseSchedules(List<ScheduleDTO> schedules,
                                          Commission commission,
                                          List<Classroom> classroomsNewSubject) {

        List<Schedule> res = new ArrayList<Schedule>();

        for( int i=0;i<classroomsNewSubject.size(); i++ ) {
            res.add(this.parseSchedule(schedules.get(i), commission ,classroomsNewSubject.get(i)));
        }

        return res;
    }

    private Schedule parseSchedule(ScheduleDTO sch, Commission commission, Classroom classroom) {
        Schedule schedule = new Schedule();
        schedule.setDay(Day.valueOf(this.toDayEnumString(sch.getDay())));
        schedule.setStartTime(sch.getStartTime());
        schedule.setEndTime(sch.getEndTime());
        schedule.setCommission(commission);
        // classroom
        classroom.addSchedule(schedule);
        schedule.setClassroom(classroom);
        schedule.setNotices(new ArrayList<OverlapNotice>());

        return schedule;
    }

    // auxiliary methods

    private String toDayEnumString(String day) {
        String dayString = new String();
        switch (day) {
            case "Lunes": dayString = "LUNES";
            break;
            case "Martes": dayString = "MARTES";
            break;
            case "Miércoles": dayString = "MIERCOLES";
            break;
            case "Jueves": dayString = "JUEVES";
            break;
            case "Viernes": dayString = "VIERNES";
            break;
            case "Sábado": dayString = "SABADO";
            break;
        }

        return  dayString;
    }

    private String toEnumSemesterString(String semester) {
        String res = new String();
        switch (semester) {
            case "Primer":
            case "Primer cuatrimestre":
                res = "PRIMER";
                break;
            case "Segundo":
            case "Segundo cuatrimestre":
                res = "SEGUNDO";
                break;
            case "Anual": res = "ANUAL";
                break;
        }
        return res;
    }

    public List<String> getClassroomNumbers() {

        List<String> res = new ArrayList<>();
        for(CommissionDTO com : this.getCommissions()) {
            res.addAll(this.getClassroomNumberByCommission(com.getSchedules()));
        }
        return res;
    }

    private List<String> getClassroomNumberByCommission(List<ScheduleDTO> schedules) {
        List<String> res = new ArrayList<>();
        for(ScheduleDTO sch : schedules) {
            res.add(sch.getClassroom().getNumber());
        }
        return res;
    }
}

package com.misaulasunq.service;

import com.misaulasunq.controller.dto.CommissionDTO;
import com.misaulasunq.controller.dto.GeneralInfo;
import com.misaulasunq.controller.wrapper.SubjectFilterRequestWrapper;
import com.misaulasunq.exceptions.InvalidDayException;
import com.misaulasunq.exceptions.InvalidSemesterException;
import com.misaulasunq.exceptions.SubjectNotFoundException;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.Commission;
import com.misaulasunq.model.Day;
import com.misaulasunq.model.Subject;
import com.misaulasunq.persistance.SubjectDAO;
import com.misaulasunq.persistance.SubjectRepository;
import com.misaulasunq.utils.CommissionUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectDAO subjectDAO;

    @Autowired
    private CommissionUpdater commissionUpdater;

    public List<String> retrieveSubjectsSuggestions() {
        return this.subjectRepository.getAllSubjectsNames();
    }

    public List<Subject> retreiveSubjectsByFilterCriteria(SubjectFilterRequestWrapper subjectFilterRequestWrapper) throws SubjectNotFoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectDAO.getSubjectByFilterRequest(subjectFilterRequestWrapper),
                SubjectNotFoundException.SubjectNotFoundByCriteria()
        );
    }

    @Deprecated
    public List<Subject> retreiveSubjectsWithSchedulesBetween(LocalTime startTime, LocalTime endTime) throws SubjectNotFoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectsBetweenHours(startTime, endTime),
                SubjectNotFoundException.SubjectNotFoundBetween(startTime,endTime)
            );
    }

    @Deprecated
    public List<Subject> retreiveSubjectsWithName(String name) throws SubjectNotFoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectByName(name),
                SubjectNotFoundException.SubjectNotFoundByName(name)
            );
    }

    @Deprecated
    public List<Subject> retreiveSubjectsInClassroom(String classroomnumber) throws SubjectNotFoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectThatAreInClassroom(classroomnumber),
                SubjectNotFoundException.SubjectNotFoundByNumber(classroomnumber)
            );
    }

    @Deprecated
    public List<Subject> retreiveSubjectsDictatedOnDay(Day currentDay) throws SubjectNotFoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.getAllSubjectsDictatedInTheDay(currentDay),
                SubjectNotFoundException.SubjectNotFoundCurrentDay()
        );
    }

    private List<Subject> returnSubjectsOrExceptionIfEmpty(List<Subject> subjects, SubjectNotFoundException exception) throws SubjectNotFoundException {
        if (subjects.isEmpty()){
            throw exception;
        }
        return subjects;
    }


    public void saveSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    public Subject findSubjectById(Integer id) throws SubjectNotFoundException {
        return subjectRepository.findById(id).orElseThrow(() -> new SubjectNotFoundException(id));
    }

    public Page<Subject> getPageSubject(Pageable pageable) {
        return subjectRepository.findAllByOrderByNameAsc(pageable);
    }

    public void deleteAll() { subjectRepository.deleteAll(); }

    public Subject editGeneralInfo(Integer id, GeneralInfo generalInfo) throws SubjectNotFoundException {
        Subject retrievedSubjectById = this.findSubjectById(id);
        retrievedSubjectById.setName(generalInfo.getName());
        retrievedSubjectById.setSubjectCode(generalInfo.getSubjectCode());

        this.saveSubject(retrievedSubjectById);

        return retrievedSubjectById;
    }

    // OBS: Esto deberia ser del commission service!
    public List<Commission> getCommissionsById(Integer id) throws SubjectNotFoundException {
        return this.findSubjectById(id).getCommissions();
    }


    // OBS: Esto deberia ser del commission service!
    public void updateCommissions(Subject subjectById,
                                  List<Commission> subjectCommission,
                                  List<CommissionDTO> commissionsDTO,
                                  Map<String, Classroom> classroomMap) throws InvalidDayException, InvalidSemesterException {
        commissionUpdater.update(subjectCommission, commissionsDTO, classroomMap);
        this.saveSubject(subjectById);
    }

    public Page<Subject> getOverlappingSubjects(Pageable pageable) {
        return this.subjectRepository.findOverlappingSubjects(pageable);
    }
}

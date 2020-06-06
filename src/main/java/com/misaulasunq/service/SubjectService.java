package com.misaulasunq.service;

import com.misaulasunq.controller.dto.CommissionDTO;
import com.misaulasunq.controller.dto.GeneralInfo;
import com.misaulasunq.exceptions.SubjectNotfoundException;
import com.misaulasunq.model.Commission;
import com.misaulasunq.model.Day;
import com.misaulasunq.model.Subject;
import com.misaulasunq.persistance.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<String> retrieveSubjectsSuggestions() {
        return this.subjectRepository.getAllSubjectsNames();
    }

    public List<Subject> retreiveSubjectsWithSchedulesBetween(LocalTime startTime, LocalTime endTime) throws SubjectNotfoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectsBetweenHours(startTime, endTime),
                SubjectNotfoundException.SubjectNotFoundBetween(startTime,endTime)
            );
    }

    public List<Subject> retreiveSubjectsWithName(String name) throws SubjectNotfoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectByName(name),
                SubjectNotfoundException.SubjectNotFoundByName(name)
            );
    }

    public List<Subject> retreiveSubjectsInClassroom(String classroomnumber) throws SubjectNotfoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectThatAreInClassroom(classroomnumber),
                SubjectNotfoundException.SubjectNotFoundByNumber(classroomnumber)
            );
    }

    private List<Subject> returnSubjectsOrExceptionIfEmpty(List<Subject> subjects, SubjectNotfoundException exception) throws SubjectNotfoundException {
        if (subjects.isEmpty()){
            throw exception;
        }
        return subjects;
    }

    //TODO: estos metodos pueden deprecarse
    public void saveSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    public Subject findSubjectById(Integer id) throws SubjectNotfoundException {
        return subjectRepository.findById(id).orElseThrow(() -> new SubjectNotfoundException(id));
    }

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public void deleteAll() { subjectRepository.deleteAll(); }

    public List<Subject> retreiveSubjectsDictatedOnDay(Day currentDay) throws SubjectNotfoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.getAllSubjectsDictatedInTheDay(currentDay),
                SubjectNotfoundException.SubjectNotFoundCurrentDay()
        );
    }

    public Subject editGeneralInfo(Integer id, GeneralInfo generalInfo) throws SubjectNotfoundException {
        Subject retrievedSubjectById = this.findSubjectById(id);
        retrievedSubjectById.setName(generalInfo.getName());
        retrievedSubjectById.setSubjectCode(generalInfo.getSubjectCode());

        this.saveSubject(retrievedSubjectById);

        return retrievedSubjectById;
    }

    public List<Commission> getCommissionsById(Integer id) throws SubjectNotfoundException {
        return this.findSubjectById(id).getCommissions();
    }

    public void updateCommissions(Subject subject, List<Commission> commissions) throws SubjectNotfoundException{
        subject.setCommissions(commissions);
        this.saveSubject(subject);
    }
}

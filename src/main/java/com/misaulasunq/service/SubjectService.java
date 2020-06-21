package com.misaulasunq.service;

import com.misaulasunq.controller.dto.GeneralInfo;
import com.misaulasunq.exceptions.SubjectNotFoundException;
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

    public List<Subject> retreiveSubjectsWithSchedulesBetween(LocalTime startTime, LocalTime endTime) throws SubjectNotFoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectsBetweenHours(startTime, endTime),
                SubjectNotFoundException.SubjectNotFoundBetween(startTime,endTime)
            );
    }

    public List<Subject> retreiveSubjectsWithName(String name) throws SubjectNotFoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectByName(name),
                SubjectNotFoundException.SubjectNotFoundByName(name)
            );
    }

    public List<Subject> retreiveSubjectsInClassroom(String classroomnumber) throws SubjectNotFoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectThatAreInClassroom(classroomnumber),
                SubjectNotFoundException.SubjectNotFoundByNumber(classroomnumber)
            );
    }

    private List<Subject> returnSubjectsOrExceptionIfEmpty(List<Subject> subjects, SubjectNotFoundException exception) throws SubjectNotFoundException {
        if (subjects.isEmpty()){
            throw exception;
        }
        return subjects;
    }
    // TODO: Aca habria que agregar un chequeo para que revise si no hay un horario que se solape y generar la nota si es necesario
    public void saveSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    public Subject findSubjectById(Integer id) throws SubjectNotFoundException {
        return subjectRepository.findById(id).orElseThrow(() -> new SubjectNotFoundException(id));
    }

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public void deleteAll() { subjectRepository.deleteAll(); }

    public List<Subject> retreiveSubjectsDictatedOnDay(Day currentDay) throws SubjectNotFoundException {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.getAllSubjectsDictatedInTheDay(currentDay),
                SubjectNotFoundException.SubjectNotFoundCurrentDay()
        );
    }

    public Subject editGeneralInfo(Integer id, GeneralInfo generalInfo) throws SubjectNotFoundException {
        Subject retrievedSubjectById = this.findSubjectById(id);
        retrievedSubjectById.setName(generalInfo.getName());
        retrievedSubjectById.setSubjectCode(generalInfo.getSubjectCode());

        this.saveSubject(retrievedSubjectById);

        return retrievedSubjectById;
    }

    public List<Commission> getCommissionsById(Integer id) throws SubjectNotFoundException {
        return this.findSubjectById(id).getCommissions();
    }

    public void updateCommissions(Subject subject, List<Commission> commissions) throws SubjectNotFoundException {
        subject.setCommissions(commissions);
        this.saveSubject(subject);
    }
}

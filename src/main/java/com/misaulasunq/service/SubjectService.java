package com.misaulasunq.service;

import com.misaulasunq.exceptions.SubjectNotfoundException;
import com.misaulasunq.model.Subject;
import com.misaulasunq.persistance.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> retreiveSubjectsWithSchedulesBetween(LocalTime startTime, LocalTime endTime) {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectsBetweenHours(startTime, endTime),
                SubjectNotfoundException.SubjectNotFoundBetween(startTime,endTime)
            );
    }

    public List<Subject> retreiveSubjectsWithName(String name) {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectByName(name),
                SubjectNotfoundException.SubjectNotFoundByName(name)
            );
    }

    public List<Subject> retreiveSubjectsInClassroom(String classroomnumber){
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findSubjectThatAreInClassroom(classroomnumber),
                SubjectNotfoundException.SubjectNotFoundByNumber(classroomnumber)
            );
    }

    private List<Subject> returnSubjectsOrExceptionIfEmpty(List<Subject> subjects, SubjectNotfoundException exception){
        if (subjects.isEmpty()){
            throw exception;
        }
        return subjects;
    }

    //TODO: estos metodos pueden deprecarse
    public void saveSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    public Subject findSubjectById(Integer id) {
        return subjectRepository.findById(id).orElseThrow(() -> new SubjectNotfoundException(id));
    }

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public void deleteAll() { subjectRepository.deleteAll(); }

    public List<Subject> retreiveSubjectsCurrentDay(DayOfWeek currentDay) {
        return this.returnSubjectsOrExceptionIfEmpty(
                this.subjectRepository.findCurrentDaySubjects(currentDay),
                SubjectNotfoundException.SubjectNotFoundCurrentDay()
        );
    }
}

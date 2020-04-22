package com.misaulasunq.service;

import com.misaulasunq.exceptions.SubjectNotfoundException;
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

    public List<Subject> retreiveSubjectsWithSchedulesBetween(LocalTime startTime, LocalTime endTime) {
        List<Subject> subjects = this.subjectRepository.findSubjectsBetweenHours(startTime, endTime);

        if(subjects.isEmpty()){
            throw SubjectNotfoundException.SubjectNotFoundBetween(startTime,endTime);
        }

        return subjects;
    }

    public List<Subject> retreiveSubjectsWithName(String name) {
        List<Subject> subjects = this.subjectRepository.findSubjectByName(name);

        if(subjects.isEmpty()){
            throw SubjectNotfoundException.SubjectNotFoundByName(name);
        }

        return subjects;
    }

    public List<Subject> retreiveSubjectsInClassroom(String classroomnumber){
        List<Subject> subjects = this.subjectRepository.findSubjectThatAreInClassroom(classroomnumber);

        if (subjects.isEmpty()){
            throw SubjectNotfoundException.SubjectNotFoundByNumber(classroomnumber);
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

}

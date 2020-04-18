package com.misaulasunq.service;

import com.misaulasunq.exceptions.SubjectNotfoundException;
import com.misaulasunq.model.Subject;
import com.misaulasunq.persistance.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> retreiveSubjectsInClassroom(String classroomnumber){
        List<Subject> subjects = subjectRepository.findSubjectThatAreInClassroom(classroomnumber);

        if (subjects.isEmpty()){
            throw new SubjectNotfoundException(classroomnumber);
        }

        return subjects;
    }


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

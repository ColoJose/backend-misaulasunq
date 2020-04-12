package com.misaulasunq.services;

import com.misaulasunq.exceptions.SubjectNotfoundException;
import com.misaulasunq.model.Subject;
import com.misaulasunq.service.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

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

package services;

import model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectRepository {

    @Autowired
    private SubjectRepository subjectRepository;

    public void save(Subject subject) {
        subjectRepository.save(subject);
    }
}

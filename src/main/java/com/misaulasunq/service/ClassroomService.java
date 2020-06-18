package com.misaulasunq.service;
import com.misaulasunq.exceptions.ClassroomNotFoundException;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.persistance.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    public List<String> retrieveClassroomSuggestions() {
        return this.classroomRepository.getAllClassroomsNumbers();
    }

    public Classroom findClassroomByNumber(String number) throws ClassroomNotFoundException {
        return this.classroomRepository.findByNumber(number);
    }

    public List<Classroom> findClassroomsByNumber(List<String> classroomNumbers) {
        return this.classroomRepository.getClassroomByNumbers(classroomNumbers);
    }
}

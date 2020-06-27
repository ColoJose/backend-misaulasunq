package com.misaulasunq.service;
import com.misaulasunq.exceptions.ClassroomNotFoundException;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.persistance.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    public List<String> retrieveClassroomSuggestions() {
        return this.classroomRepository.getAllClassroomsNumbers();
    }

    public Classroom findClassroomByNumber(String number) throws ClassroomNotFoundException {
        return this.classroomRepository.findClassroomByNumber(number);
    }

    public List<Classroom> findClassroomsByNumber(List<String> classroomNumbers) {
        return this.classroomRepository.getClassroomByNumbers(classroomNumbers);
    }

    public Map<String, Classroom> getClassroomMap(Set<String> allClassroomsNumbers) throws ClassroomNotFoundException {
        Map<String, Classroom> classroomMap = new HashMap<>();
        for (String number : allClassroomsNumbers) {
            classroomMap.put(number, findClassroomByNumber(number));
        }

        return classroomMap;
    }
}

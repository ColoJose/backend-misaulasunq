package com.misaulasunq.service;
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
}

package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(
        origins = "http://localhost:3000",
        methods = RequestMethod.GET,
        maxAge = 60
)
@RestController(value = "ClassroomAPI")
@Transactional
@RequestMapping(
        name="ClassroomAPI",
        value = "/classroomAPI",
        produces = "application/json",
        method = {RequestMethod.GET}
)
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSuggestions(){
        return new ResponseEntity<List<String>>(
            classroomService.retrieveClassroomSuggestions(),
            HttpStatus.OK
        );
    }
}

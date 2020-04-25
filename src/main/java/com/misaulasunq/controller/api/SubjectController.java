package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.model.Subject;
import com.misaulasunq.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin( //Se puede configurar para que sea a travez de una clase
        origins = "http://localhost:3000",
        methods = RequestMethod.GET,
        maxAge = 60
)
@RestController(value = "SubjectAPI")
@Transactional
@RequestMapping(
        name="SubjectAPI",
        value = "/subjectAPI",
//        produces = "application/json",
        method = {RequestMethod.GET}
    )
public class SubjectController {

    @Autowired
    private SubjectService subjectService;


    @GetMapping("/byClassroomNumber/{classroomNumber}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByClassroomNumber(@PathVariable String classroomNumber ){
        List<Subject> subjectsFound = subjectService.retreiveSubjectsInClassroom(classroomNumber);

        List<SubjectDTO> subjectDTOs = subjectsFound.stream().map(SubjectDTO::new).collect(Collectors.toList());

        return new ResponseEntity<>(subjectDTOs, HttpStatus.OK);
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/byName/{name}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByName(@PathVariable String name) {
        List<Subject> subjectsFound = subjectService.retreiveSubjectsWithName(name);

        List<SubjectDTO> subjectDTOs = subjectsFound.stream().map(SubjectDTO::new).collect(Collectors.toList());

        return new ResponseEntity<>(subjectDTOs, HttpStatus.OK);
    }
}

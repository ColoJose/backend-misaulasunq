package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.DegreeDTO;
import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.exceptions.DegreeNotFoundException;
import com.misaulasunq.exceptions.SubjectNotfoundException;
import com.misaulasunq.model.Degree;
import com.misaulasunq.model.Subject;
import com.misaulasunq.model.SubjectToParse;
import com.misaulasunq.service.DegreeService;
import com.misaulasunq.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(//Se puede configurar para que sea a travez de una clase
        origins = "http://localhost:3000",
        methods = {RequestMethod.GET, RequestMethod.POST},
        maxAge = 60
)
@RestController(value = "SubjectAPI")
@Transactional
@RequestMapping(
        name="SubjectAPI",
        value = "/subjectAPI",
        produces = "application/json",
        method = {RequestMethod.GET}
    )
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private DegreeService degreeService;

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSuggestions(){
        return new ResponseEntity<List<String>>(this.subjectService.retrieveSubjectsSuggestions(),HttpStatus.OK);
    }

    @GetMapping("/byClassroomNumber/{classroomNumber}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByClassroomNumber(@PathVariable String classroomNumber ) throws SubjectNotfoundException {
        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsInClassroom(classroomNumber)
            );
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByName(@PathVariable String name) throws SubjectNotfoundException {
        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsWithName(name)
            );
    }

    @GetMapping("/betweenHours/{start}/{end}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsBetweenHours(@PathVariable String start, @PathVariable String end) throws SubjectNotfoundException {
        LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ISO_LOCAL_TIME);

        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsWithSchedulesBetween(startTime, endTime)
            );
    }

    @GetMapping("/currentDaySubjectsAA")
    public ResponseEntity<List<SubjectDTO>> getSubjectsCurrentDay() throws SubjectNotfoundException {
        DayOfWeek currentDay =  LocalDate.now().getDayOfWeek();
            return this.makeResponseEntityWithGoodStatus(
                    this.subjectService.retreiveSubjectsCurrentDay(currentDay)
            );
    }

    @PostMapping(value = "/new-subject", consumes = "application/json")
    public ResponseEntity createNewSubject( @RequestBody SubjectToParse subjectToParse) throws DegreeNotFoundException {

        Degree degreeReceived = this.degreeService.findDegreeById(subjectToParse.getDegreeId());

        Subject subject = subjectToParse.parse(degreeReceived);
        // recibo el subjectDto y se lo doy a DTOparser
        // el dtoparser recibe el subjectDTO y pide todas las carreras y le setea subject las carreras
        // desp agarro las comisiones y las tranformo en una comision del dominio
        // cuando parseo los horarios de las comisones, armo los horarios
        //

        this.subjectService.saveSubject(subject);
        return new ResponseEntity<>("Materia creada correctamente",HttpStatus.OK);
    }

    @GetMapping("/all-degrees")
    public ResponseEntity<List<DegreeDTO>> getAllDegrees() {

        List<Degree> allDegrees = this.degreeService.findAll();
        return new ResponseEntity<>(
                allDegrees.stream()
                                 .map(DegreeDTO::new)
                                 .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @GetMapping("all")

    private ResponseEntity<List<SubjectDTO>> makeResponseEntityWithGoodStatus(List<Subject> subjects){
        return new ResponseEntity<>(
                subjects.stream()
                        .map(SubjectDTO::new)
                        .collect(Collectors.toList()),
                HttpStatus.OK
            );
    }
}

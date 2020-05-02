package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.model.Subject;
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
        methods = RequestMethod.GET,
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



    @GetMapping("/byClassroomNumber/{classroomNumber}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByClassroomNumber(@PathVariable String classroomNumber ){
        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsInClassroom(classroomNumber)
            );
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByName(@PathVariable String name) {
        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsWithName(name)
            );
    }

    @GetMapping("/betweenHours/{start}/{end}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsBetweenHours(@PathVariable String start, @PathVariable String end) {
        LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ISO_LOCAL_TIME);

        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsWithSchedulesBetween(startTime, endTime)
            );
    }

    @GetMapping("/currentDaySubjects")
    public ResponseEntity<List<SubjectDTO>> getSubjectsCurrentDay(){
        DayOfWeek currentDay =  LocalDate.now().getDayOfWeek();
            return this.makeResponseEntityWithGoodStatus(
                    this.subjectService.retreiveSubjectsCurrentDay(currentDay)
            );
    }

    private ResponseEntity<List<SubjectDTO>> makeResponseEntityWithGoodStatus(List<Subject> subjects){
        return new ResponseEntity<>(
                subjects.stream()
                        .map(SubjectDTO::new)
                        .collect(Collectors.toList()),
                HttpStatus.OK
            );
    }
}

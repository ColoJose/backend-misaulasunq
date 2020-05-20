package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.exceptions.SubjectNotfoundException;
import com.misaulasunq.model.Subject;
import com.misaulasunq.service.SubjectService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RestController(value = "SubjectAPI")
@CrossOrigin(//Se puede configurar para que sea a travez de una clase
        origins = "http://localhost:3000",
        methods = RequestMethod.GET,
        maxAge = 60
)
@RequestMapping(
        name="SubjectAPI",
        value = "/subjectAPI",
        produces = "application/json",
        method = {RequestMethod.GET}
    )
@Api(tags = "Subject Endpoint", value = "SubjectEndpoint", description = "Controller para las materias. Se puede consultar sugerencias, busquedas de materias y creacion de las mismas.")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/suggestions")
    @ApiOperation(value = "Devuelve una lista de sugerencia de las materias disponibles para buscar.")
    public ResponseEntity<List<String>> getSuggestions(){
        return new ResponseEntity<List<String>>(this.subjectService.retrieveSubjectsSuggestions(),HttpStatus.OK);
    }

    @GetMapping("/byClassroomNumber/{classroomNumber}")
    @ApiOperation(value = "Realiza una busqueda de materias por el numero de aula.")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No subjects in the classroom {classroomNumber}.")})
    public ResponseEntity<List<SubjectDTO>> getSubjectsByClassroomNumber(
                        @ApiParam(required = true, value = "Es el numero del aula por la cual se va a buscar", example = "CyT-1")
                        @PathVariable String classroomNumber ) throws SubjectNotfoundException {
        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsInClassroom(classroomNumber)
            );
    }

    @GetMapping("/byName/{name}")
    @ApiOperation(value = "Realiza una busqueda de materias por el nombre de la materia.")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No subjects with the Name {name}.")})
    public ResponseEntity<List<SubjectDTO>> getSubjectsByName(
                    @ApiParam(required = true, value = "Nombre de la materia a buscar",example = "Matematica I")
                    @PathVariable String name ) throws SubjectNotfoundException {
        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsWithName(name)
            );
    }

    @GetMapping("/betweenHours/{start}/{end}")
    @ApiOperation(value = "Realiza una busqueda de materias donde dicten clases entre una determinada franja horaria")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No subjects Between Hours {start} - {end}.")})
    public ResponseEntity<List<SubjectDTO>> getSubjectsBetweenHours(
                    @ApiParam(required = true, value = "Hora de inicio de franja horaria de la busqueda.",example = "13:00")
                    @PathVariable String start,
                    @ApiParam(required = true, value = "Hora de fin de franja horaria de la busqueda.",example = "22:00")
                    @PathVariable String end) throws SubjectNotfoundException {
        LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ISO_LOCAL_TIME);

        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsWithSchedulesBetween(startTime, endTime)
            );
    }

    @GetMapping("/currentDaySubjects")
    @ApiOperation(value = "Devuelve las materias que se dictan en el dia.")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No subjects in the current day")})
    public ResponseEntity<List<SubjectDTO>> getSubjectsCurrentDay() throws SubjectNotfoundException {
        DayOfWeek currentDay =  LocalDate.now().getDayOfWeek();
            return this.makeResponseEntityWithGoodStatus(
                    this.subjectService.retreiveSubjectsCurrentDay(currentDay)
            );
    }

    @PostMapping("/newsubject")
    public ResponseEntity createNewSubject(@Valid @RequestBody Subject subject) {

        return ResponseEntity.ok("subject successfully created");
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

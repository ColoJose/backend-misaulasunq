package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.DegreeDTO;
import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.exceptions.DegreeNotFoundException;
import com.misaulasunq.exceptions.SubjectNotfoundException;
import com.misaulasunq.model.Degree;
import com.misaulasunq.model.Day;
import com.misaulasunq.model.Subject;
import com.misaulasunq.model.SubjectToParse;
import com.misaulasunq.service.DegreeService;
import com.misaulasunq.service.SubjectService;
import com.misaulasunq.utils.DayConverter;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Transactional
@RestController(value = "SubjectAPI")
@CrossOrigin(//Se puede configurar para que sea a travez de una clase
        origins = "http://localhost:3000",
        methods = {RequestMethod.GET, RequestMethod.POST},
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

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SubjectService subjectService;


    @Autowired
    private DegreeService degreeService;

    @GetMapping("/byDay/{aDay}")
    @ApiOperation(value = "Devuelve las materias que son dictadas en el dia {aDay}")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No subjects in that day")})
    public ResponseEntity<List<SubjectDTO>> getSubjectsDictatedOnTheDay(
            @ApiParam(required = true, value = "Es el Nombre Del Dia a Buscar (Ej: Lunes,Martes,Sabado, etc)", example = "Lunes")
            @PathVariable String aDay) throws SubjectNotfoundException {
        LOGGER.info("Got a GET request to retrieve subjects that are dictated in the day: {}",aDay);

        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsDictatedOnDay(Day.valueOf(aDay.toUpperCase()))
        );
    }


    @GetMapping("/suggestions")
    @ApiOperation(value = "Devuelve una lista de sugerencia de las materias disponibles para buscar.")
    public ResponseEntity<List<String>> getSuggestions(){
        LOGGER.info("Got a GET request to retrieve subject suggestions");
        ResponseEntity<List<String>> response = new ResponseEntity<>(
                this.subjectService.retrieveSubjectsSuggestions(),
                HttpStatus.OK);
        LOGGER.info("Responding the request with suggestions: {}", response);
        return response;
    }

    @GetMapping("/byClassroomNumber/{classroomNumber}")
    @ApiOperation(value = "Realiza una busqueda de materias por el numero de aula.")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No subjects in the classroom {classroomNumber}.")})
    public ResponseEntity<List<SubjectDTO>> getSubjectsByClassroomNumber(
                        @ApiParam(required = true, value = "Es el numero del aula por la cual se va a buscar", example = "CyT-1")
                        @PathVariable String classroomNumber ) throws SubjectNotfoundException {
        LOGGER.info("Got a GET request to retrieve subjects that are dictated in the classroom {}",classroomNumber);
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
        LOGGER.info("Got a GET request to retrieve subjects with the name {}",name);
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
        LOGGER.info("Got a GET request to retrieve subjects that are dictated between {} and {}",start,end);
        LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ISO_LOCAL_TIME);

        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsWithSchedulesBetween(startTime, endTime)
            );
    }


    @GetMapping("/currentDaySubjects")
    @ApiOperation(value = "Devuelve las materias que se dictan en el dia.")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No subjects in that day")})
    public ResponseEntity<List<SubjectDTO>> getSubjectsCurrentDay() throws SubjectNotfoundException {
        LOGGER.info("Got a GET request to retrieve subjects that are dictated in the current day");
        DayOfWeek currentDay =  LocalDate.now().getDayOfWeek();
        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsDictatedOnDay(DayConverter.getDay(currentDay))
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

    @GetMapping("/all-subjects")
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.getAll()
        );
    }

    private ResponseEntity<List<SubjectDTO>> makeResponseEntityWithGoodStatus(List<Subject> subjects){
        ResponseEntity<List<SubjectDTO>> response = new ResponseEntity<>(
                subjects.stream()
                        .map(SubjectDTO::new)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );

        LOGGER.info("Responding the request with: {}", response);

        return response;
    }
}

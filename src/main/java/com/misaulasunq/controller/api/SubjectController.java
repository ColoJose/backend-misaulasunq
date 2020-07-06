package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.*;
import com.misaulasunq.exceptions.*;
import com.misaulasunq.model.*;
import com.misaulasunq.service.ClassroomService;
import com.misaulasunq.service.DegreeService;
import com.misaulasunq.service.SubjectService;
import com.misaulasunq.utils.DayConverter;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;



@Transactional
@RestController(value = "SubjectAPI")
@CrossOrigin(//Se puede configurar para que sea a travez de una clase
        origins = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT},
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

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/OverlappingSubjects")
    public ResponseEntity<Page<SubjectDTO>> getOverlappingSubjects(
            @RequestParam(name="page") Integer page,
            @RequestParam(name="elements") Integer elements
    ) {
        return this.makeResponseEntityWithGoodStatusForPage(
                this.subjectService.getOverlappingSubjects(PageRequest.of(page, elements))
        );
    }


    @GetMapping("/byDay/{aDay}")
    @ApiOperation(value = "Devuelve las materias que son dictadas en el dia {aDay}")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No subjects in that day")})
    public ResponseEntity<List<SubjectDTO>> getSubjectsDictatedOnTheDay(
            @ApiParam(required = true, value = "Es el Nombre Del Dia a Buscar (Ej: Lunes,Martes,Sabado, etc)", example = "Lunes")
            @PathVariable String aDay) throws SubjectNotFoundException {
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
                        @PathVariable String classroomNumber ) throws SubjectNotFoundException {
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
                    @PathVariable String name ) throws SubjectNotFoundException {
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
                    @PathVariable String end) throws SubjectNotFoundException {
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
    public ResponseEntity<List<SubjectDTO>> getSubjectsCurrentDay() throws SubjectNotFoundException {
        LOGGER.info("Got a GET request to retrieve subjects that are dictated in the current day");
        DayOfWeek currentDay =  LocalDate.now().getDayOfWeek();
        return this.makeResponseEntityWithGoodStatus(
                this.subjectService.retreiveSubjectsDictatedOnDay(DayConverter.getDay(currentDay))
            );
    }

    @PostMapping(value = "/new-subject", consumes = "application/json")
    public ResponseEntity createNewSubject( @RequestBody SubjectToParse subjectToParse) throws DegreeNotFoundException {

        Degree degreeReceived = this.degreeService.findDegreeById(subjectToParse.getDegreeId());
        List<Classroom> classroomsNewSubject = this.classroomService
                                                   .findClassroomsByNumber(
                                                        subjectToParse.getClassroomNumbers()
                                                   );

        Subject subject = subjectToParse.parse(degreeReceived, classroomsNewSubject);

        this.subjectService.saveSubject(subject);
        return new ResponseEntity<>("Materia creada correctamente",HttpStatus.OK);
    }

    @GetMapping(value = "/all-degrees") //TODO: Esto tendria que estar ser parte de un Degree Controller
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
    public ResponseEntity<Page<SubjectDTO>> getAllSubjects(
            @RequestParam(name="page") Integer page,
            @RequestParam(name="elems") Integer elems
    ) {
        Page aPageRequested = this.subjectService.getPageSubject(PageRequest.of(page, elems));
        List<Subject> subjects = aPageRequested.getContent();
        List<SubjectDTO> subjectPage =  subjects.stream()
                                               .map(SubjectDTO::new)
                                               .collect(Collectors.toList());

        PagedListHolder aPage = new PagedListHolder<SubjectDTO>(subjectPage);
        return new ResponseEntity<Page<SubjectDTO>>(
                                new PageImpl<SubjectDTO>(
                                        subjectPage,
                                        aPageRequested.getPageable(),
                                        aPageRequested.getTotalElements()
                                )
                                ,HttpStatus.OK);
    }

    @PutMapping(value = "/edit-general-info/{id}", consumes = "application/json")
    public ResponseEntity<SubjectDTO> editGeneralInfoSubject(@PathVariable Integer id,
                                                             @RequestBody GeneralInfo generalInfo)
                                                             throws SubjectNotFoundException {
        Subject retrievedSubject = this.subjectService.editGeneralInfo(id, generalInfo);
        SubjectDTO subjectDTO = new SubjectDTO(retrievedSubject);

        return new ResponseEntity<SubjectDTO>(subjectDTO, HttpStatus.OK);
    }

    @GetMapping("/commissions/{id}") //TODO: Esto tendria que estar ser parte de un Commission Controller
    public ResponseEntity<List<CommissionDTO>> getCommissionById(@PathVariable Integer id) throws SubjectNotFoundException {
        List<CommissionDTO> commissionsById = this.subjectService.getCommissionsById(id)
                                                                 .stream()
                                                                 .map(CommissionDTO::new)
                                                                 .collect(Collectors.toList());

        return new ResponseEntity<>(commissionsById,HttpStatus.OK);
    }

    @PutMapping(value = "/edit/commissions/{id}", consumes = "application/json") //TODO: Esto tendria que estar ser parte de un Commission Controller
    public ResponseEntity<String> updateCommission(@PathVariable Integer id,
                                                   @RequestBody List<CommissionDTO> commissionsDTO)
            throws SubjectNotFoundException, ClassroomNotFoundException, InvalidDayException, InvalidSemesterException {

        Map<String, Classroom> classroomMap = this.classroomService.getClassroomMap(this.getAllClassroomsNumbers(commissionsDTO));
        Subject subjectById = this.subjectService.findSubjectById(id);
        List<Commission> subjectCommission = this.subjectService.getCommissionsById(id);
        this.subjectService.updateCommissions( subjectById,subjectCommission, commissionsDTO, classroomMap);

//        List<Commission> parsedCommissions = commissionParser.parseCommissions(commissions,subjectById,classroomMap); TODO borrar

        return new ResponseEntity<>("Comisiones materia actualizada",HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-subject-codes")
    public ResponseEntity<List<String>> getAllSubjectCodes() {
        return new ResponseEntity<>(this.subjectService.getAllSubjectCodes(), HttpStatus.OK);
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


    // aux methods

    private Set<String> getAllClassroomsNumbers(List<CommissionDTO> commissions) {

        Set<String> result = new HashSet<String>();

        for ( CommissionDTO com : commissions ) {
            result.addAll( this.getClassroomsSchedules(com.getSchedules()) );
        }

        return result;
    }

    private Set<String> getClassroomsSchedules(List<ScheduleDTO> schedules) {
        Set<String> res = new HashSet<String>();
        for (ScheduleDTO sch: schedules) {
            res.add(sch.getClassroom().getNumber());
        }
        return res;
    }

    private ResponseEntity<Page<SubjectDTO>> makeResponseEntityWithGoodStatusForPage(Page<Subject> subjects){
        List<SubjectDTO> dtoList = subjects.stream()
                                        .map(SubjectDTO::new)
                                        .collect(Collectors.toList());
        Page aPage = new PageImpl<SubjectDTO>(dtoList, subjects.getPageable(), subjects.getTotalElements());

        ResponseEntity<Page<SubjectDTO>> response = new ResponseEntity<>(aPage,HttpStatus.OK);

        LOGGER.info("Responding the request with: {}", response);
        return response;

    }
}

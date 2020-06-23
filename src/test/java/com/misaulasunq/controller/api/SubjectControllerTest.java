package com.misaulasunq.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.misaulasunq.TestConfig;
import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.exceptions.SubjectNotFoundException;
import com.misaulasunq.model.*;
import com.misaulasunq.persistance.SubjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Transactional
public class SubjectControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubjectController subjectController;

    @Autowired
    private SubjectRepository subjectRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(subjectController)
                .build();
    }

    @Test
    public void ifTryToGetSubjectsByDayAndHaveOneGetAGoodResponse() throws SubjectNotFoundException {
        //Setup(Given)
        ResponseEntity<List<SubjectDTO>> response;

        //Exercise(When)
        response = subjectController.getSubjectsDictatedOnTheDay("SABADO");

        //Test(Then)
        assertEquals(HttpStatus.OK, response.getStatusCode(),"No tiene que haber error en el request!");
        assertEquals(5,
                Objects.requireNonNull(response.getBody()).size(),
                "No trajo ningun subject! Revisar el service o Repository"
        );
    }

    @Test
    public void ifTryToGetSubjectsByDayAndNotHaveOneGetAError(){
        //Setup(Given)
        String exceptionMessage = "";

        //Exercise(When)
        try {
            subjectController.getSubjectsDictatedOnTheDay("Domingo");
        } catch (SubjectNotFoundException subjectNotfoundException){
            exceptionMessage = subjectNotfoundException.getMessage();
        }
        //Test(Then)
        assertEquals("No subjects in the current day", exceptionMessage);
    }

    @Test
    public void getAEmptyListIfDontHaveSubjectSuggestions(){
        //Setup(Given)
        subjectRepository.deleteAll();

        //Exercise(When)
        ResponseEntity<List<String>> response = subjectController.getSuggestions();

        //Test(Then)
        assertEquals(200,
                response.getStatusCodeValue(),
                "Tiene que ser una respuesta del servicio correcta");
        assertTrue("No tiene que haber sugerencias!",
                response.getBody().isEmpty());
    }

    @Test
    public void ifHaveSubjectSuggestionsTheirAreRetrieved(){
        //Setup(Given)

        //Exercise(When)
        ResponseEntity<List<String>> response = subjectController.getSuggestions();

        //Test(Then)
        assertEquals(200,
                response.getStatusCodeValue(),
                "Tiene que ser una respuesta del servicio correcta");
        assertFalse("Tiene que haber sugerencias de materias en la respuesta!",
                response.getBody().isEmpty());
    }

    @Test
    public void ifGetSubjectSuggestionsGetAGoodResponse() throws Exception{
        //Test(then)
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/subjectAPI/suggestions")
                ).andExpect(status().isOk())
                 .andExpect(content().string(containsString("Matematica")))
                 .andExpect(content().string(containsString("TIP")));
    }

    @Test
    public void ifGetSubjectASubjectBetweenHoursGetAGoodResponse() throws SubjectNotFoundException {
        //Setup(Given)
        ResponseEntity<List<SubjectDTO>> response;

        //Exercise(When)
        response = subjectController.getSubjectsBetweenHours("09:00","13:00");

        //Test(Then)
        assertEquals(HttpStatus.OK, response.getStatusCode(),"No tiene que haber error en el request!");
        assertEquals(6,
                Objects.requireNonNull(response.getBody()).size(),
                "No trajo ningun subject! Revisar el service o Repository"
        );
    }

    @Test
    public void ifGetSubjectsBetweenHoursAndDontHaveNoneOne_getAError(){
        //Setup(Given)
        String exceptionMessage = "";

        //Exercise(When)
        try {
            subjectController.getSubjectsBetweenHours("00:00","01:00");
        } catch (SubjectNotFoundException subjectNotfoundException){
            exceptionMessage = subjectNotfoundException.getMessage();
        }
        //Test(Then)
        assertEquals("No subjects Between Hours 00:00 - 01:00.", exceptionMessage);
    }

    @Test
    public void ifGetSubjectByName_getAGoodResponse() throws SubjectNotFoundException {
        //Setup(Given)
        ResponseEntity<List<SubjectDTO>> response;

        //Exercise(When)
        response = subjectController.getSubjectsByName("Sistemas Operativos");

        //Test(Then)
        assertEquals(HttpStatus.OK, response.getStatusCode(),"No tiene que haber error en el request!");
        assertEquals(
                1,
                Objects.requireNonNull(response.getBody()).size(),
                "No trajo ningun subject! Revisar el service o Repository"
        );

        SubjectDTO subejctDTOAnswered = response.getBody().get(0);

        assertEquals(
                "Sistemas Operativos",
                subejctDTOAnswered.getName(),
                "No trajo la materia correcta o no convirtio correctamente el nombre");
        assertEquals(
                "150",
                subejctDTOAnswered.getSubjectCode(),
                "No trajo la materia correcta o no convirtio correctamente el codigo de materia");
        assertEquals(
                1,
                subejctDTOAnswered.getCommissions().size(),
                "No convirtio correctamente las comisiones de la materia");
    }

    @Test
    public void ifGetSubjectByANonExistentNameGetAError(){
        //Setup(Given)
        String exceptionMessage = "";

        //Exercise(When)
        try {
            subjectController.getSubjectsByName("Unreachable Name");
        } catch (SubjectNotFoundException subjectNotfoundException){
            exceptionMessage = subjectNotfoundException.getMessage();
        }
        //Test(Then)
        assertEquals("No subjects with the Name Unreachable Name.", exceptionMessage);
    }

    @Test
    public void ifGetSubjectGetAGoodResponse() throws SubjectNotFoundException {
        //Setup(Given)
        ResponseEntity<List<SubjectDTO>> response;

        //Exercise(When)
        response = subjectController.getSubjectsByClassroomNumber("52");

        //Test(Then)
        assertEquals( HttpStatus.OK, response.getStatusCode(),"No tiene que haber error en el request!");
        assertEquals(
                6,
                Objects.requireNonNull(response.getBody()).size(),
                "No trajo ningun subject! Revisar el service o Repository"
            );
    }

    @Test
    public void ifGetSubjectByANonExistentClassroomNumberGetAError(){
        //Setup(Given)
        String exceptionMessage = "";

        //Exercise(When)
        try {
            subjectController.getSubjectsByClassroomNumber("999");
        } catch (SubjectNotFoundException subjectNotfoundException){
            exceptionMessage = subjectNotfoundException.getMessage();
        }
        //Test(Then)
        assertEquals("No subjects in the classroom 999.", exceptionMessage);
    }
//OBS: El test rompe por que intenta escribir el objeto como un string y entra en una recursion infinita por la bidireccionalidad de las relaciones (Ej: materia tiene carreras y carreras tiene materias), es mucho mas coherente armar un json de como se espera recibir el objeto.
//    @Test
//    public void whenRequestToCreateNewSubjectWithAValidOne_getStatusOk() throws Exception{
//
//        Subject subject = this.validSubject();
//        String serializedSubject = objectMapper.writeValueAsString(subject);
//
//        this.mockMvc.perform(
//                MockMvcRequestBuilders
//                        .post("subjectAPI/newrequest")
//                        .contentType("application/json")
//                        .content(serializedSubject))
//                        .andExpect(status().isOk());
//    }

    // auxiliary methods

    private Subject validSubject() {

        // valid schedule
        Schedule schedule = new Schedule();
        schedule.setStartTime(LocalTime.now());
        schedule.setEndTime(LocalTime.now());
        schedule.setDay(Day.LUNES);

        // valid commission
        Commission commission = new Commission();
        commission.setName("com 1");
        commission.setYear(2020);
        commission.setSemester(Semester.PRIMER);
        commission.addSchedule(schedule);

        // valid degree
        Degree degree = new Degree();
        degree.setName("tpi");

        // set up valid subject
        Subject validSubject = new Subject();
        validSubject.setName("so");
        validSubject.setSubjectCode("so007");
        validSubject.addCommission(commission);
        validSubject.addDegree(degree);

        degree.addSubject(validSubject);

        return validSubject;
    }


}
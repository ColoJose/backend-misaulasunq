package com.misaulasunq.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.exceptions.SubjectNotfoundException;
import com.misaulasunq.model.*;
import com.misaulasunq.service.SubjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SubjectController.class)
@Rollback
public class SubjectControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubjectController subjectController;

    @Autowired
    private MockMvc mockMvc;



    @Test
    public void ifGetSubjectASubjectBetweenHours_getAGoodResponse() throws SubjectNotfoundException {
        //Setup(Given)
        ResponseEntity<List<SubjectDTO>> response;

        //Exercise(When)
        response = subjectController.getSubjectsBetweenHours("09:00","13:00");

        //Test(Then)
        assertEquals("No tiene que haber error en el request!", HttpStatus.OK, response.getStatusCode());
        assertEquals(
                "No trajo ningun subject! Revisar el service o Repository",
                3,
                Objects.requireNonNull(response.getBody()).size()
        );
    }

    @Test
    public void ifGetSubjectsBetweenHoursAndDontHaveNoneOne_getAError(){
        //Setup(Given)
        String exceptionMessage = "";

        //Exercise(When)
        try {
            subjectController.getSubjectsBetweenHours("00:00","01:00");
        } catch (SubjectNotfoundException subjectNotfoundException){
            exceptionMessage = subjectNotfoundException.getMessage();
        }
        //Test(Then)
        assertEquals("No subjects Between Hours 00:00 - 01:00.", exceptionMessage);
    }

    @Test
    public void ifGetSubjectByName_getAGoodResponse() throws SubjectNotfoundException {
        //Setup(Given)
        ResponseEntity<List<SubjectDTO>> response;

        //Exercise(When)
        response = subjectController.getSubjectsByName("Sistemas Operativos");

        //Test(Then)
        assertEquals("No tiene que haber error en el request!", HttpStatus.OK, response.getStatusCode());
        assertEquals(
                "No trajo ningun subject! Revisar el service o Repository",
                1,
                Objects.requireNonNull(response.getBody()).size()
        );

        SubjectDTO subejctDTOAnswered = response.getBody().get(0);

        assertEquals("No trajo la materia correcta o no convirtio correctamente el nombre",
                "Sistemas Operativos",
                subejctDTOAnswered.getName());
        assertEquals("No trajo la materia correcta o no convirtio correctamente el codigo de materia",
                "150",
                subejctDTOAnswered.getSubjectCode());
        assertEquals("No convirtio correctamente las comisiones de la materia",
                1,
                subejctDTOAnswered.getCommissions().size());
    }

    @Test
    public void ifGetSubjectByAUnexistName_getAError(){
        //Setup(Given)
        String exceptionMessage = "";

        //Exercise(When)
        try {
            subjectController.getSubjectsByName("Unreachable Name");
        } catch (SubjectNotfoundException subjectNotfoundException){
            exceptionMessage = subjectNotfoundException.getMessage();
        }
        //Test(Then)
        assertEquals("No subjects with the Name Unreachable Name.", exceptionMessage);
    }

    @Test
    public void ifGetSubject_getAGoodResponse() throws SubjectNotfoundException {
        //Setup(Given)
        ResponseEntity<List<SubjectDTO>> response;

        //Exercise(When)
        response = subjectController.getSubjectsByClassroomNumber("52");

        //Test(Then)
        assertEquals("No tiene que haber error en el request!", HttpStatus.OK, response.getStatusCode());
        assertEquals(
                "No trajo ningun subject! Revisar el service o Repository",
                2,
                Objects.requireNonNull(response.getBody()).size()
            );
    }

    @Test
    public void ifGetSubjectByAUnexistClassroomNumber_getAError(){
        //Setup(Given)
        String exceptionMessage = "";

        //Exercise(When)
        try {
            subjectController.getSubjectsByClassroomNumber("999");
        } catch (SubjectNotfoundException subjectNotfoundException){
            exceptionMessage = subjectNotfoundException.getMessage();
        }
        //Test(Then)
        assertEquals("No subjects in the classroom 999.", exceptionMessage);
    }

    @Test
    public void whenRequestToCreateNewSubjectWithAValidOne_getStatusOk() throws Exception{

        Subject subject = this.validSubject();
        String serializedSubject = objectMapper.writeValueAsString(subject);

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("subjectAPI/newrequest")
                        .contentType("application/json")
                        .content(serializedSubject))
                        .andExpect(status().isOk());
    }

    @Test
    public void whenReceivingAValidSubject_shouldRetrieveHttpStatusOk() {

    }

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
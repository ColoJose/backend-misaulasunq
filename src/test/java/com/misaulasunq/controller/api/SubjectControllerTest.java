package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.exceptions.SubjectNotfoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
public class SubjectControllerTest {

    @Autowired
    private SubjectController subjectController;

    @Test
    public void ifGetSubjectASubjectBetweenHours_getAGoodResponse(){
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
    public void ifGetSubjectByName_getAGoodResponse(){
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
    public void ifGetSubject_getAGoodResponse(){
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
}
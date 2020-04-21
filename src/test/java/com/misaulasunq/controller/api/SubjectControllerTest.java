package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.exceptions.SubjectNotfoundException;
import com.misaulasunq.model.*;
import com.misaulasunq.persistance.DegreeRepository;
import com.misaulasunq.utils.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
public class SubjectControllerTest {

    @Autowired
    private SubjectController subjectController;
    @Autowired
    private DegreeRepository degreeRepository;

    @Test
    public void ifGetSubjectByName_getAGoodResponse(){
        //Setup(Given)
        ResponseEntity<List<SubjectDTO>> response;

        //Exercise(When)
        response = subjectController.getSubjectsByName("ASubject");

        //Test(Then)
        assertEquals("No tiene que haber error en el request!", HttpStatus.OK, response.getStatusCode());
        assertEquals(
                "No trajo ningun subject! Revisar el service o Repository",
                1,
                Objects.requireNonNull(response.getBody()).size()
        );

        SubjectDTO subejctDTOAnswered = response.getBody().get(0);

        assertEquals("No trajo la materia correcta o no convirtio correctamente el nombre",
                "ASubject",
                subejctDTOAnswered.getName());
        assertEquals("No trajo la materia correcta o no convirtio correctamente el codigo de materia",
                "1",
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
        response = subjectController.getSubjectsByClassroomNumber("34");

        //Test(Then)
        assertEquals("No tiene que haber error en el request!", HttpStatus.OK, response.getStatusCode());
        assertEquals(
                "No trajo ningun subject! Revisar el service o Repository",
                1,
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

    @Before
    public void setUp(){
        Degree aDegree = DegreeBuilder.buildADegree().withMockData().build();
        Subject aSubject = SubjectBuilder.buildASubject().withName("ASubject")
                .withSubjectCode("1")
                .withDegrees(new ArrayList<>(List.of(aDegree)))
                .build();
        aDegree.addSubject(aSubject);
        Classroom classroom34 = ClassroomBuilder.buildAClassroom().withName("34").build();
        Schedule aSchedule = ScheduleBuilder.buildASchedule().withMockData()
                .withClassroom(classroom34)
                .build();
        classroom34.addSchedule(aSchedule);
        Commission aCommission = CommissionBuilder.buildACommission().withMockData()
                .withSchedules(new ArrayList<>(List.of(aSchedule)))
                .withSubject(aSubject)
                .build();
        aSubject.addCommission(aCommission);
        aSchedule.setCommission(aCommission);
        degreeRepository.save(aDegree);
    }

}
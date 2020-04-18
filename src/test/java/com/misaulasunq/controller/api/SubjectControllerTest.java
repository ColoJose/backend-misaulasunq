package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.SubjectDTO;
import com.misaulasunq.model.*;
import com.misaulasunq.persistance.DegreeRepository;
import com.misaulasunq.utils.*;
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
    public void ifGetSubject_getAGoodResponse(){
        //Setup(Given)
        ResponseEntity<List<SubjectDTO>> response;

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

}
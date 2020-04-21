package com.misaulasunq.service;

import com.misaulasunq.exceptions.SubjectNotfoundException;
import com.misaulasunq.model.*;
import com.misaulasunq.persistance.*;
import com.misaulasunq.utils.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Transactional
public class SubjectServiceTest {

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    public SubjectService subjectService;

    @Before
    public void setUp(){
        //Degree
        Degree testDegree = DegreeBuilder.buildADegree().withMockData().build();

        //Subjects
        Subject funcional = SubjectBuilder.buildASubject().withName("Funcional")
                .withSubjectCode("153")
                .withDegrees(new ArrayList<>(List.of(testDegree)))
                .build();
        Subject concurrente = SubjectBuilder.buildASubject().withName("Concurrente")
                .withSubjectCode("135")
                .withDegrees(new ArrayList<>(List.of(testDegree)))
                .build();
        testDegree.addSubject(funcional);
        testDegree.addSubject(concurrente);

        // Classrooms
        Classroom aula5 = ClassroomBuilder.buildAClassroom().withName("5").build();
        Classroom aula2 = ClassroomBuilder.buildAClassroom().withName("2").build();

        // Schedules
        Schedule concurrenteSchedule = ScheduleBuilder.buildASchedule().withMockData()
                .withClassroom(aula5)
                .build();
        Schedule funcionalSchedule = ScheduleBuilder.buildASchedule().withMockData()
                .withClassroom(aula2)
                .build();

        aula5.addSchedule(concurrenteSchedule);
        aula2.addSchedule(funcionalSchedule);

        // Commissions
        Commission funcionalC1 = CommissionBuilder.buildACommission().withMockData()
                .withSchedules(new ArrayList<>(List.of(funcionalSchedule)))
                .withSubject(funcional)
                .build();
        Commission concurrenteC1 = CommissionBuilder.buildACommission().withMockData()
                .withSchedules(new ArrayList<>(List.of(concurrenteSchedule)))
                .withSubject(concurrente)
                .build();
        funcional.addCommission(funcionalC1);
        funcionalSchedule.setCommission(funcionalC1);
        concurrente.addCommission(concurrenteC1);
        concurrenteSchedule.setCommission(concurrenteC1);
        degreeRepository.save(testDegree);

    }

    @Test
    public void ifTryToGetASubjectWithAName_AndThereIsOne_ItsRetrieved() {
        //Setup(Given)

        //exercise
        List<Subject> subjects = subjectService.retreiveSubjectsWithName("Funcional");

        //test
        assertEquals("Debe tener solo una materia!",1,subjects.size());
        assertEquals("No trajo la materia con el nombre 'Funcional'",
                "Funcional",
                subjects.get(0).getName());
    }

    @Test
    public void ifTryToGetASubjectWithAName_AndThereIsNone_GetAClassroomNotFoundException(){
        //Setup(Given)
        String exceptionMessage = "";

        //Exercise(When)
        try {
            subjectService.retreiveSubjectsWithName("Redes");
        } catch (SubjectNotfoundException subjectNotfoundException){
            exceptionMessage = subjectNotfoundException.getMessage();
        }

        //Test(Then)
        assertEquals("No hubo excepcion de materias no encontradas por nombre!",
                "No subjects with the Name Redes.",
                exceptionMessage
        );
    }

    @Test
    public void ifHaveSubjectInClassroomFive_IfSearchWithClassroomNumber_WeGetOne() {
        //Setup(Given)

        //exercise
        List<Subject> subjectsInClassroom5 = subjectService.retreiveSubjectsInClassroom("5");

        //test
        assertEquals("Debe tener solo una materia!",1,subjectsInClassroom5.size());

        Subject subjectRetrieved = subjectsInClassroom5.get(0);
        assertEquals("Debe traer la materia Concurrente!",
                "Concurrente",
                subjectRetrieved.getName());
        assertEquals("Debe traer la materia Concurrente!",
                1,
                subjectsInClassroom5.size());
        assertEquals("El Subject debe tener solo una comision!",
                1,
                subjectRetrieved.getCommissions().size());

        Classroom classroomOfSubject = subjectRetrieved.getCommissions()
                .get(0).getSchedules()
                .get(0).getClassroom();
        assertEquals("Tiene que ser el aula 5!","5",classroomOfSubject.getNumber());
    }

    @Test
    public void ifTryToRetrieveASubjectInClassroomFive_AndThereIsNone_GetAClassroomNotFoundException() {
        //Setup(Given)
        String exceptionMessage = "";

        //Exercise(When)
        try {
            subjectService.retreiveSubjectsInClassroom("666");
        } catch (SubjectNotfoundException subjectNotfoundException){
            exceptionMessage = subjectNotfoundException.getMessage();
        }

        //Test(Then)
        assertEquals("No hubo excepcion de materias no encontradas!",
                "No subjects in the classroom 666.",
                exceptionMessage
        );
    }
}
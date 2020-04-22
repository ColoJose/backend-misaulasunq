package com.misaulasunq.persistance;

import com.misaulasunq.model.*;
import com.misaulasunq.utils.*;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Transactional //Para que no commitee a la base!
public class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    public void ifHaveSubjectsWithHoursBetween19HoursAnd23Hours_TheirAreRetrieved(){
        //Setup(Given)
        LocalTime startTimeToSearch = LocalTime.of(1,0);
        LocalTime endTimeToSearch = LocalTime.of(4,0);

        // Exercise (When)
        List<Subject> subjectsRetrieved = subjectRepository.findSubjectsBetweenHours(
                                                                startTimeToSearch,
                                                                endTimeToSearch
                                                        );

        // Test (then)
        assertEquals("Tiene que haber solo dos materias entre esos horarios!", 2, subjectsRetrieved.size());
        LocalTime startTime;
        LocalTime endTime;
        for (Subject each : subjectsRetrieved){
            startTime = each.getCommissions().get(0).getSchedules().get(0).getStartTime();
            endTime = each.getCommissions().get(0).getSchedules().get(0).getEndTime();
            assertTrue("No esta entre los horarios de la Query!",
                (startTime.isAfter(startTimeToSearch) && startTime.isBefore(endTimeToSearch))
                     || (endTime.isAfter(startTimeToSearch) && endTime.isBefore(endTimeToSearch))
            );
        }
    }

    @Test
    public void ifHaveSubjectsWithNameLikeBase_TheirAreRetrievedWhenGetBySubjectName(){
        //Setup(Given)

        // Exercise (When)
        List<Subject> subjectsRetrieved = subjectRepository.findSubjectByName("Base De Datos");

        // Test (then)
        assertEquals("Solo existe una materia con el nombre exacto", 1, subjectsRetrieved.size());
        assertEquals("Tiene que tener la materia Base De Datos. La query esta mal o el nombre del dato de prueba erroneo",
                "Base De Datos",
                subjectsRetrieved.get(0).getName());
    }

    @Test
    public void whenSaveSubjectTheIdShouldBeDistinctToZero(){
        //Setup (Given)
        Subject subjectToSUT = SubjectBuilder.buildASubject()
                                            .withName("TIP")
                                            .withSubjectCode("2020")
                                            .build();

        //Exercise (When)
        subjectRepository.save(subjectToSUT);
        Subject savedSubject = subjectRepository.getOne(subjectToSUT.getId());

        //Test (Then)
        assertNotNull(
                "No tiene que ser null, tiene que haberle generado un id al insertarlo!",
                savedSubject.getId()
            );
        assertEquals(
                "Se altero el Nombre, no debe hacerlo",
                "TIP",
                savedSubject.getName()
            );
        assertEquals(
                "Se altero el Codigo de materia, no debe hacerlo",
                "2020",
                savedSubject.getSubjectCode()
            );
    }

    @Test
    public void whenSaveSeveralSubjectInDbFindAllShouldRetrieveThisEntities(){
        //Setup (Given)
        Subject FPSubject = SubjectBuilder.buildASubject()
                                        .withName("Programacion Funcional")
                                        .withSubjectCode("201")
                                        .build();
        Subject OOP1ProgSubject = SubjectBuilder.buildASubject()
                                        .withName("Programacion Orientada A Objetos 1")
                                        .withSubjectCode("1004")
                                        .build();
        Subject OOP2ProgSubject = SubjectBuilder.buildASubject()
                                            .withName("Programacion Orientada A Objetos 2")
                                            .withSubjectCode("1005")
                                            .build();
        List<Subject> subjectsToSave = List.of(FPSubject,OOP1ProgSubject,OOP2ProgSubject);

        //Exercise (When)
        subjectRepository.saveAll(subjectsToSave);
        List<Subject> subjectsRetrieved = List.of(
                subjectRepository.findById(FPSubject.getId()).get(),
                subjectRepository.findById(OOP1ProgSubject.getId()).get(),
                subjectRepository.findById(OOP2ProgSubject.getId()).get()
        );

        //Test (Then)
        for (int i = 0; i < 3; i++) {
            assertNotNull(
                    "No tiene que ser null, tiene que haberle generado un id al insertarlo!",
                    subjectsRetrieved.get(i).getId()
            );
            assertEquals(
                    "Se altero el Nombre, no debe hacerlo",
                    subjectsToSave.get(i).getName(),
                    subjectsRetrieved.get(i).getName()
            );
            assertEquals(
                    "Se altero el Codigo de materia, no debe hacerlo",
                    subjectsToSave.get(i).getSubjectCode(),
                    subjectsRetrieved.get(i).getSubjectCode()
            );
        }
    }

    @Before
    public void setUp(){
        //Degree
        Degree testDegree = DegreeBuilder.buildADegree().withMockData().build();

        //Subjects
        Subject desap = SubjectBuilder.buildASubject().withName("Desarrollo de Aplicaciones")
                .withSubjectCode("33")
                .withDegrees(new ArrayList<>(List.of(testDegree)))
                .build();

        Subject BBD = SubjectBuilder.buildASubject().withName("Base De Datos")
                .withSubjectCode("153")
                .withDegrees(new ArrayList<>(List.of(testDegree)))
                .build();
        Subject BDDII = SubjectBuilder.buildASubject().withName("Base de Datos II")
                .withSubjectCode("333")
                .withDegrees(new ArrayList<>(List.of(testDegree)))
                .build();
        testDegree.addSubject(BBD);
        testDegree.addSubject(BDDII);
        testDegree.addSubject(desap);

        // Classrooms
        Classroom aula522 = ClassroomBuilder.buildAClassroom().withName("522").build();
        Classroom aula20 = ClassroomBuilder.buildAClassroom().withName("20").build();

        // Schedules
        Schedule desapSchedule = ScheduleBuilder.buildASchedule().withMockData()
                .withClassroom(aula522)
                .withStartTime(LocalTime.of(1,30))
                .withEndTime(LocalTime.of(4,30))
                .build();
        Schedule BBDSchedule = ScheduleBuilder.buildASchedule().withMockData()
                .withClassroom(aula20)
                .withStartTime(LocalTime.of(1,0))
                .withEndTime(LocalTime.of(3,30))
                .build();
        Schedule BDDIISchedule = ScheduleBuilder.buildASchedule().withMockData()
                .withClassroom(aula20)
                .withStartTime(LocalTime.of(12,0))
                .withEndTime(LocalTime.of(16,0))
                .build();

        aula522.addSchedule(desapSchedule);
        aula20.addSchedule(BBDSchedule);
        aula20.addSchedule(BDDIISchedule);

        // Commissions
        Commission desapC1 = CommissionBuilder.buildACommission().withMockData()
                .withSchedules(new ArrayList<>(List.of(desapSchedule)))
                .withSubject(desap)
                .build();
        Commission BBDC1 = CommissionBuilder.buildACommission().withMockData()
                .withSchedules(new ArrayList<>(List.of(BBDSchedule)))
                .withSubject(BBD)
                .build();
        Commission BDDIIC1 = CommissionBuilder.buildACommission().withMockData()
                .withSchedules(new ArrayList<>(List.of(BDDIISchedule)))
                .withSubject(BDDII)
                .build();
        desap.addCommission(desapC1);
        desapSchedule.setCommission(desapC1);
        BBD.addCommission(BBDC1);
        BBDSchedule.setCommission(BBDC1);
        BDDII.addCommission(BDDIIC1);
        BDDIISchedule.setCommission(BDDIIC1);

        subjectRepository.saveAll(List.of(desap,BBD,BDDII));
    }
}

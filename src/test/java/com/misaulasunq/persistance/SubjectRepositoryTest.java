package com.misaulasunq.persistance;

import com.misaulasunq.model.*;
import com.misaulasunq.utils.*;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@Rollback
@DataJpaTest
@Transactional
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubjectRepositoryTest {

    @Autowired private SubjectRepository subjectRepository;
    @Autowired private DegreeRepository degreeRepository;
    @Autowired private ClassroomRepository classroomRepository;
    @Autowired private OverlapNoticeRepository overlapNoticeRepository;

    @Test
    public void ifFindeTheFirst2OverlappingSubjectsAndHave3_OnlyGetsTwoSubjects() {
        //Setup(Given)
        Degree aDegree = this.degreeRepository.findAll().get(0);
        Optional<Classroom> optionalClassroom = this.classroomRepository.findClassroomsByNumberEquals("20");
        Classroom aClassroom = optionalClassroom.get();
        Subject aSubject = SubjectBuilder.buildASubject().withMockData().withDegree(aDegree).withSubjectCode("444").build();
        Subject aSubject1 = SubjectBuilder.buildASubject().withMockData().withDegree(aDegree).withSubjectCode("445").build();
        Subject aSubject2 = SubjectBuilder.buildASubject().withMockData().withDegree(aDegree).withSubjectCode("446").build();
        Subject aSubject3 = SubjectBuilder.buildASubject().withMockData().withDegree(aDegree).withSubjectCode("447").build();
        aDegree.addSubject(aSubject);
        aDegree.addSubject(aSubject1);
        aDegree.addSubject(aSubject2);
        aDegree.addSubject(aSubject3);

        Commission commission1 = CommissionBuilder.buildACommission().withMockData().withSubject(aSubject).build();
        aSubject.addCommission(commission1);
        Commission commission2 = CommissionBuilder.buildACommission().withMockData().withSubject(aSubject1).build();
        aSubject1.addCommission(commission2);
        Commission commission3 = CommissionBuilder.buildACommission().withMockData().withSubject(aSubject2).build();
        aSubject2.addCommission(commission3);
        Commission commission4 = CommissionBuilder.buildACommission().withMockData().withSubject(aSubject3).build();
        aSubject3.addCommission(commission4);

        Schedule schedule1 = ScheduleBuilder.buildASchedule().withMockData().withCommission(commission1).withClassroom(aClassroom).build();
        commission1.addSchedule(schedule1);
        aClassroom.addSchedule(schedule1);
        Schedule schedule2 = ScheduleBuilder.buildASchedule().withMockData().withCommission(commission2).withClassroom(aClassroom).build();
        commission2.addSchedule(schedule2);
        aClassroom.addSchedule(schedule2);
        Schedule schedule3 = ScheduleBuilder.buildASchedule().withMockData().withCommission(commission3).withClassroom(aClassroom).build();
        commission3.addSchedule(schedule3);
        aClassroom.addSchedule(schedule3);
        Schedule schedule4 = ScheduleBuilder.buildASchedule().withMockData().withCommission(commission4).withClassroom(aClassroom).build();
        commission4.addSchedule(schedule4);
        aClassroom.addSchedule(schedule4);

        OverlapNotice aOverlap = OverlapNoticeBuilder.buildAOverlapNotice().withClassroom(aClassroom).withScheduleAffected(schedule1).withScheduleConflicted(schedule2).build();
        OverlapNotice aOverlap1 = OverlapNoticeBuilder.buildAOverlapNotice().withClassroom(aClassroom).withScheduleAffected(schedule2).withScheduleConflicted(schedule3).build();
        OverlapNotice aOverlap2 = OverlapNoticeBuilder.buildAOverlapNotice().withClassroom(aClassroom).withScheduleAffected(schedule3).withScheduleConflicted(schedule4).build();

        this.subjectRepository.saveAll(List.of(aSubject1, aSubject2, aSubject3, aSubject));
        this.overlapNoticeRepository.saveAll(List.of(aOverlap,aOverlap1,aOverlap2));

        //Exercise(When)
        Page<Subject> subjectsRetrieved = this.subjectRepository.findOverlappingSubjects(PageRequest.of(0,2));

        //Test(Then)
        assertEquals(2,subjectsRetrieved.getNumberOfElements());
        assertEquals(2,subjectsRetrieved.getTotalPages());
        assertEquals(3,subjectsRetrieved.getTotalElements());
    }

    @Test
    public void ifFindOverlappingSubjectAndNotHaveOne_ReturnEmptyList() {
        //Exercise(When)
        Page<Subject>subjectsRetrieved = this.subjectRepository.findOverlappingSubjects(PageRequest.of(4,2));

        //Test(Then)
        assertFalse(subjectsRetrieved.hasContent());
    }

    @Test
    public void ifHaveSubjectInTheDataBase_TheirNameAreRetrieved(){
        //Setup(Given)

        //Exercise(When)
        List<String> subjectsName = subjectRepository.getAllSubjectsNames();

        //Test(Then)
        assertFalse("Tiene que traer algunos nombres de materias!",
                subjectsName.isEmpty());
        assertEquals(3,subjectsName.size());
        assertTrue("Tiene que contener el nombre de la materia",
                subjectsName.contains("Desarrollo de Aplicaciones"));
        assertTrue("Tiene que contener el nombre de la materia",
                subjectsName.contains("Base De Datos"));
        assertFalse("No tiene que contener un numero de aula",
                subjectsName.contains("CyT-1"));
    }

    @Test
    public void ifHaveSubjectsWithHoursBetween8HoursAnd22HoursTheirAreRetrieved(){
        //Setup(Given)
        LocalTime startTimeToSearch = LocalTime.of(8,0);
        LocalTime endTimeToSearch = LocalTime.of(22,0);

        // Exercise (When)
        List<Subject> subjectsRetrieved = subjectRepository.findSubjectsBetweenHours(
                startTimeToSearch,
                endTimeToSearch
        );

        // Test (then)
        assertEquals("Tiene que haber solo una materia entre esos horarios!",1, subjectsRetrieved.size());
        LocalTime startTime = subjectsRetrieved.get(0).getCommissions().get(0).getSchedules().get(0).getStartTime();
        LocalTime endTime = subjectsRetrieved.get(0).getCommissions().get(0).getSchedules().get(0).getEndTime();
        assertTrue("No esta entre los horarios de la Query!",
                (startTime.isAfter(startTimeToSearch) && startTime.isBefore(endTimeToSearch))
                        || (endTime.isAfter(startTimeToSearch) && endTime.isBefore(endTimeToSearch))
        );
    }

    @Test
    public void ifHaveSubjectsWithHoursBetween19HoursAnd23HoursTheirAreRetrieved(){
        //Setup(Given)
        LocalTime startTimeToSearch = LocalTime.of(1,0);
        LocalTime endTimeToSearch = LocalTime.of(4,0);

        // Exercise (When)
        List<Subject> subjectsRetrieved = subjectRepository.findSubjectsBetweenHours(
                                                                startTimeToSearch,
                                                                endTimeToSearch
                                                        );

        // Test (then)
        assertEquals("Tiene que haber solo dos materias entre esos horarios!",2, subjectsRetrieved.size());
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
    public void ifHaveSubjectsWithNameLikeBaseTheirAreRetrievedWhenGetBySubjectName(){
        //Setup(Given)

        // Exercise (When)
        List<Subject> subjectsRetrieved = subjectRepository.findSubjectByName("Base De Datos");

        // Test (then)
        assertEquals("Solo existe una materia con el nombre exacto",1, subjectsRetrieved.size());
        assertEquals("Tiene que tener la materia Base De Datos. La query esta mal o el nombre del dato de prueba erroneo",
                "Base De Datos",
                subjectsRetrieved.get(0).getName());
    }

    @Test
    public void whenSaveSubjectTheIdShouldBeDistinctToZero(){
        //Setup (Given)
        Degree aDegree = DegreeBuilder.buildADegree().withMockData().build();
        Commission aCommission = CommissionBuilder.buildACommission().withMockData().build();
        Schedule aSchedule = ScheduleBuilder.buildASchedule().withMockData().withCommission(aCommission).build();
        aCommission.addSchedule(aSchedule);
        Subject subjectToSUT = SubjectBuilder.buildASubject()
                                            .withName("TIP")
                                            .withSubjectCode("2020")
                                            .build();
        subjectToSUT.addDegree(aDegree);
        subjectToSUT.addCommission(aCommission);
        aCommission.setSubject(subjectToSUT);
        aDegree.addSubject(subjectToSUT);

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
        Degree aDegree = DegreeBuilder.buildADegree().withMockData().build();
        Commission aCommission = CommissionBuilder.buildACommission().withMockData().build();
        Schedule aSchedule = ScheduleBuilder.buildASchedule().withMockData().withCommission(aCommission).build();
        aCommission.addSchedule(aSchedule);
        Subject FPSubject = SubjectBuilder.buildASubject()
                                        .withName("Programacion Funcional")
                                        .withSubjectCode("201")
                                        .build();
        FPSubject.addDegree(aDegree);
        FPSubject.addCommission(aCommission);
        aCommission.setSubject(FPSubject);
        aDegree.addSubject(FPSubject);

        Commission aCommission1 = CommissionBuilder.buildACommission().withMockData().build();
        Schedule aSchedule1 = ScheduleBuilder.buildASchedule().withMockData().withCommission(aCommission1).build();
        aCommission1.addSchedule(aSchedule1);
        Subject OOP1ProgSubject = SubjectBuilder.buildASubject()
                                        .withName("Programacion Orientada A Objetos 1")
                                        .withSubjectCode("1004")
                                        .build();
        OOP1ProgSubject.addDegree(aDegree);
        OOP1ProgSubject.addCommission(aCommission1);
        aCommission1.setSubject(OOP1ProgSubject);
        aDegree.addSubject(OOP1ProgSubject);

        Commission aCommission2 = CommissionBuilder.buildACommission().withMockData().build();
        Schedule aSchedule2 = ScheduleBuilder.buildASchedule().withMockData().withCommission(aCommission2).build();
        aCommission2.addSchedule(aSchedule2);
        Subject OOP2ProgSubject = SubjectBuilder.buildASubject()
                                            .withName("Programacion Orientada A Objetos 2")
                                            .withSubjectCode("1005")
                                            .build();
        OOP2ProgSubject.addDegree(aDegree);
        OOP2ProgSubject.addCommission(aCommission2);
        aCommission2.setSubject(OOP2ProgSubject);
        aDegree.addSubject(OOP2ProgSubject);

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

    @Test
    public void ifFindBySubjectCodeOfTwoSubjects_OnlyTheseAreRetrieved() {
        //Exercise(When)
        List<Subject> subjectRetrieved = subjectRepository.findAllBySubjectCodeInOrderBySubjectCodeAsc(List.of("33","333"));

        //Test(Then)
        assertEquals(2, subjectRetrieved.size());
        assertEquals("33", subjectRetrieved.get(0).getSubjectCode());
        assertEquals("333", subjectRetrieved.get(1).getSubjectCode());
    }

    @Before
    public void setUp(){
        //Degree
        Degree testDegree = DegreeBuilder.buildADegree().withMockData().withDegreeCode("958").build();

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

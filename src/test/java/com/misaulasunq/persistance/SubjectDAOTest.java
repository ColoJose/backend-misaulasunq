package com.misaulasunq.persistance;

import com.misaulasunq.model.*;
import com.misaulasunq.utils.*;
import com.misaulasunq.controller.wrapper.SubjectFilterRequestWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Rollback
@Transactional
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class SubjectDAOTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectDAO subjectDAO;

    @Test
    public void whenFilterByDayAndNameAndMatchAnySubject_ItsRetrieved(){
        //Seto(Given)
        this.loadSampleDataForTest();
        List<SearchFilter> filters =
                List.of(
                        SearchFilter.BY_SUBJECT,
                        SearchFilter.BY_DAY
                );

        SubjectFilterRequestWrapper wrapper = new SubjectFilterRequestWrapper();
        wrapper.setSearchFilters(filters);
        wrapper.setDay(Day.LUNES);
        wrapper.setSubjectName("Desarrollo de Aplicaciones");

        //Exercise(When)
        List<Subject> result = this.subjectDAO.getSubjectByFilterRequest(wrapper);

        //Test(then)
        assertEquals(1,result.size(),"Tiene que retornar una materia");
    }

    @Test
    public void whenFilterByAllFilterTypesAndOneSubjectMatch_ItsRetrieved(){
        //Setup(Given)
        this.loadSampleDataForTest();
        List<SearchFilter> filters =
                List.of(
                        SearchFilter.BY_DAY,
                        SearchFilter.BY_CLASSROOM,
                        SearchFilter.BY_SUBJECT,
                        SearchFilter.BY_SCHEDULE
                );

        SubjectFilterRequestWrapper wrapper = new SubjectFilterRequestWrapper();
        wrapper.setSearchFilters(filters);
        wrapper.setDay(Day.JUEVES);
        wrapper.setClassroomNumber("20");
        wrapper.setSubjectName("Base de Datos II");
        wrapper.setStartTime(LocalTime.of(13,0));
        wrapper.setEndTime(LocalTime.of(14,0));

        //Exercise(When)
        List<Subject> result = this.subjectDAO.getSubjectByFilterRequest(wrapper);

        //Test(then)
        assertEquals(1,result.size(),"Tiene que traer una materia por que los filtros matchean con una!");
        assertEquals("Base de Datos II",result.get(0).getName(),"No es la materia");
    }

    @Test
    public void whenFilterByAllFilterTypesAndDontHaveAnyMatchingSubject_ReturnEmpty(){
        //Seto(Given)
        List<SearchFilter> filters =
                List.of(
                        SearchFilter.BY_SUBJECT,
                        SearchFilter.BY_SCHEDULE,
                        SearchFilter.BY_CLASSROOM,
                        SearchFilter.BY_DAY
                );

        SubjectFilterRequestWrapper wrapper =
                new SubjectFilterRequestWrapper(
                        filters,
                        Day.LUNES,
                        "52",
                        "Matematica I",
                        LocalTime.of(12,0),
                        LocalTime.of(15,0)
                );

        //Exercise(When)
        List<Subject> result = this.subjectDAO.getSubjectByFilterRequest(wrapper);

        //Test(then)
        assertEquals(0,result.size(),"No tiene que retornar ninguna materia");
    }

    @Test
    public void whenFilterByDayAndClassroomAndAnyMatch_ItsRetrieved(){
        //Seto(Given)
        this.loadSampleDataForTest();
        List<SearchFilter> filters = List.of(SearchFilter.BY_CLASSROOM,SearchFilter.BY_DAY);

        SubjectFilterRequestWrapper wrapper = new SubjectFilterRequestWrapper();
        wrapper.setDay(Day.JUEVES);
        wrapper.setClassroomNumber("20");
        wrapper.setSearchFilters(filters);

        //Exercise(When)
        List<Subject> result = this.subjectDAO.getSubjectByFilterRequest(wrapper);

        //Test(then)
        assertEquals(1,result.size(),"Tiene que retornar una materia que se dicta en ese aula y entre esos horarios");
        assertEquals("Base de Datos II",result.get(0).getName(),"No es la materia");
    }

    @Test
    public void whenFilterByHoursAndNameAndAnyMatch_ItsRetrieved(){
        //Seto(Given)
        this.loadSampleDataForTest();
        List<SearchFilter> filters = List.of(SearchFilter.BY_SCHEDULE,SearchFilter.BY_SUBJECT);
        LocalTime starTime = LocalTime.of(0,0);
        LocalTime endTime = LocalTime.of(3,0);

        SubjectFilterRequestWrapper wrapper = new SubjectFilterRequestWrapper();
        wrapper.setStartTime(starTime);
        wrapper.setEndTime(endTime);
        wrapper.setSubjectName("Base De Datos");
        wrapper.setSearchFilters(filters);

        //Exercise(When)
        List<Subject> result = this.subjectDAO.getSubjectByFilterRequest(wrapper);

        //Test(then)
        assertEquals(1,result.size(),"Tiene que retornar la materia con el nombre y dictada entre esos horarios");
        assertEquals("Base De Datos",result.get(0).getName(),"No es la materia");
    }

    @Test
    public void whenFilterByHoursAndNameAndNoneMatch_getEmptyList(){
        //Seto(Given)
        this.loadSampleDataForTest();
        List<SearchFilter> filters = List.of(SearchFilter.BY_SCHEDULE,SearchFilter.BY_SUBJECT);
        LocalTime starTime = LocalTime.of(11,0);
        LocalTime endTime = LocalTime.of(15,0);

        SubjectFilterRequestWrapper wrapper = new SubjectFilterRequestWrapper();
        wrapper.setStartTime(starTime);
        wrapper.setEndTime(endTime);
        wrapper.setSubjectName("Nada");
        wrapper.setSearchFilters(filters);

        //Exercise(When)
        List<Subject> result = this.subjectDAO.getSubjectByFilterRequest(wrapper);

        //Test(then)
        assertEquals(0,result.size(),"No tiene que retornar nada");
    }

    @Test
    public void whenFilterByHoursAndOneMatch_ItsRetreived(){
        //Seto(Given)
        this.loadSampleDataForTest();
        List<SearchFilter> filters = List.of(SearchFilter.BY_SCHEDULE);
        LocalTime starTime = LocalTime.of(13,0);
        LocalTime endTime = LocalTime.of(15,0);

        SubjectFilterRequestWrapper wrapper = new SubjectFilterRequestWrapper();
        wrapper.setStartTime(starTime);
        wrapper.setEndTime(endTime);
        wrapper.setSearchFilters(filters);

        //Exercise(When)
        List<Subject> result = this.subjectDAO.getSubjectByFilterRequest(wrapper);

        //Test(then)
        assertEquals(1,result.size(),"Tiene que retornar una materia que se dicta entre esos horarios!");
        assertEquals("Base de Datos II",result.get(0).getName(),"No es la materia");
    }

    @Test
    public void whenByDayAndAnyoneMatch_ItsRetreived(){
        //Seto(Given)
        this.loadSampleDataForTest();
        List<SearchFilter> filters = List.of(SearchFilter.BY_DAY);

        SubjectFilterRequestWrapper wrapper = new SubjectFilterRequestWrapper();
        wrapper.setDay(Day.LUNES);
        wrapper.setSearchFilters(filters);

        //Exercise(When)
        List<Subject> result = this.subjectDAO.getSubjectByFilterRequest(wrapper);

        //Test(then)
        assertEquals(1,result.size(),"Tiene que retornar ninguna materia en el dia lunes!");
        assertEquals("Desarrollo de Aplicaciones",result.get(0).getName(),"No es la materia");
    }

    @Test
    public void whenFilterByNameAndAnyoneMatch_ItsRetreived(){
        //Seto(Given)
        this.loadSampleDataForTest();
        List<SearchFilter> filters = List.of(SearchFilter.BY_SUBJECT);

        SubjectFilterRequestWrapper wrapper = new SubjectFilterRequestWrapper();
        wrapper.setSubjectName("Base De Datos");
        wrapper.setSearchFilters(filters);

        //Exercise(When)
        List<Subject> result = this.subjectDAO.getSubjectByFilterRequest(wrapper);

        //Test(then)
        assertEquals(1,result.size(),"Tiene que retornar la materia Base De Datos");
        assertEquals("Base De Datos",result.get(0).getName(),"No es la materia");
    }

    @Test
    public void whenFilterByClassroomAndAnyoneMatch_ItsRetreived(){
        //Seto(Given)
        this.loadSampleDataForTest();
        List<SearchFilter> filters = List.of(SearchFilter.BY_CLASSROOM);

        SubjectFilterRequestWrapper wrapper = new SubjectFilterRequestWrapper();
        wrapper.setClassroomNumber("522");
        wrapper.setSearchFilters(filters);

        //Exercise(When)
        List<Subject> result = this.subjectDAO.getSubjectByFilterRequest(wrapper);

        //Test(then)
        assertEquals(1,result.size(),"Tiene que retornar una materia que se cursa en el aula 522");
        Classroom classroomToCheck = result.get(0).getCommissions().get(0).getSchedules().get(0).getClassroom();
        assertEquals(
                "522",
                classroomToCheck.getNumber(),
                "Trajo una materia que no tiene horario en ese aula");
    }

    public void loadSampleDataForTest(){
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
        Schedule desapSchedule = ScheduleBuilder.buildASchedule()
                .withDay(Day.LUNES)
                .withClassroom(aula522)
                .withStartTime(LocalTime.of(1,30))
                .withEndTime(LocalTime.of(4,30))
                .build();
        Schedule BBDSchedule = ScheduleBuilder.buildASchedule()
                .withDay(Day.SABADO)
                .withClassroom(aula20)
                .withStartTime(LocalTime.of(1,0))
                .withEndTime(LocalTime.of(3,30))
                .build();
        Schedule BDDIISchedule = ScheduleBuilder.buildASchedule()
                .withDay(Day.JUEVES)
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
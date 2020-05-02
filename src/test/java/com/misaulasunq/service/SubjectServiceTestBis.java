package com.misaulasunq.service;

import com.misaulasunq.model.Day;
import com.misaulasunq.model.Subject;
import com.misaulasunq.persistance.SubjectRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubjectServiceTestBis {

    @TestConfiguration
    static class SubjectServiceTestConfig {
        @Bean
        public SubjectService subjectService() {
            return new SubjectService();
        }
    }

    @Autowired
    private SubjectService subjectService;

    @MockBean
    private SubjectRepository subjectRepository;

    @Before
    public void setUp() {
        Subject obj1 = new Subject();
        obj1.setName("objetos 1");

        Mockito.when(subjectRepository.findCurrentDaySubjects(Mockito.any(Day.class))).thenReturn(Arrays.asList(obj1));
    }

    @Test
    public void whenAskServiceForSubjectOfTheCurrentDayItShoueldRetrieveIt(){

        // exercise
        List<Subject> currentDaySubject = subjectService.retreiveSubjectsCurrentDay(LocalDate.now().getDayOfWeek());
        Assert.assertEquals("objetos 1",currentDaySubject.get(0).getName());
    }
}

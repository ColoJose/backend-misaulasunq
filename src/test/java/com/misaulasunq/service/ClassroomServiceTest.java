package com.misaulasunq.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Transactional
public class ClassroomServiceTest {

    @Autowired
    public ClassroomService classroomService;

    @Test
    public void ifHaveClassroomInTheDataBaseTheirNumbersAreRetrieved(){
        //Setup(Given)
        //Exercise(When)
        //Test(Then)
        assertFalse("Tiene que traer algunos numeros de aulas!",
                classroomService.retrieveClassroomSuggestions().isEmpty());
    }
}
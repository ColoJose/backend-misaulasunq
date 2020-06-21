package com.misaulasunq.service;

import com.misaulasunq.model.Classroom;
import com.misaulasunq.persistance.ClassroomRepository;
import com.misaulasunq.utils.ClassroomBuilder;
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

    @Autowired
    public ClassroomRepository classroomRepository;

    @Test
    public void ifHaveClassroomInTheDataBaseTheirNumbersAreRetrieved(){
        //Setup(Given)
        Classroom aClassroom = ClassroomBuilder.buildAClassroom().withName("22").build();
        //Exercise(When)
        //Test(Then)
        assertFalse("Tiene que traer algunos numeros de aulas!",
                classroomService.retrieveClassroomSuggestions().isEmpty());
    }
}
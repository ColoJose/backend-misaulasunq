package com.misaulasunq.persistance;

import com.misaulasunq.RestServiceApplication;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.utils.ClassroomBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestServiceApplication.class)
@Rollback
@Transactional
public class ClassroomRepositoryTest {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Test
    public void ifDontHaveClassroomInTheDataBaseGetAEmptyList(){
        //Setup(Given)
        classroomRepository.deleteAll();
        //Exercise(When)

        //Test(Then)
        assertTrue("No tiene que traer ningun numero por que no hay aulas!",
                classroomRepository.getAllClassroomsNumbers().isEmpty());
    }

    @Test
    public void ifHaveClassroomInTheDataBaseTheirNumbersAreRetrieved(){
        //Setup(Given)

        //Exercise(When)
        List<String> classroomNumber = classroomRepository.getAllClassroomsNumbers();

        //Test(Then)
        assertFalse("Tiene que traer algunos numeros de aulas!",
                classroomNumber.isEmpty());
        assertTrue("Tiene que contener el numero de un aula",
                classroomNumber.contains("CyT-1"));
        assertTrue("Tiene que contener el numero de un aula",
                classroomNumber.contains("52"));
    }

    @Test
    public void shouldRetrieveAClassroomByNumberWhenHasOneInsertedWithTheseNumber(){
        //Start (Given)
        Classroom aula31 = ClassroomBuilder.buildAClassroom().withName("31").build();
        Classroom aula212 = ClassroomBuilder.buildAClassroom().withName("212").build();
        Classroom aula1 = ClassroomBuilder.buildAClassroom().withName("1").build();
        classroomRepository.saveAll(List.of(aula31,aula212,aula1));

        //Exercise (When)
        Optional<Classroom> classroomRetrieved = classroomRepository.findClassroomsByNumberEquals("212");

        //Test (Then)
        assertEquals("No recupero el aula dek numero 212", "212", classroomRetrieved.map(Classroom::getNumber).orElseGet(()->""));
        assertEquals("No es el mismo aula 212 que se salvo!", aula212.getId(), classroomRetrieved.map(Classroom::getId).orElseGet(()->-1));
    }

    @Test
    public void shouldNotBePresentIfRetrieveANonExistentClassroomInDB(){
        //Start (Given)

        //Exercise (When)
        Optional<Classroom> classroomRetrieved = classroomRepository.findClassroomsByNumberEquals("212");

        //Test (Then)
        assertFalse("No tiene que recuperar nada por que no hay aula!", classroomRetrieved.isPresent());
        assertEquals("No tiene que haber id por que no hay aula", "", classroomRetrieved.map(Classroom::getNumber).orElseGet(()->""));
    }
}

package com.misaulasunq.persistance;

import com.misaulasunq.model.Classroom;
import com.misaulasunq.utils.ClassroomBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Rollback
@DataJpaTest
@Transactional
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClassroomRepositoryTest {

    @Autowired private ClassroomRepository classroomRepository;

    @Test
    public void ifDontHaveClassroomInTheDataBaseGetAEmptyList(){
        //Setup(Given)
        //Exercise(When)

        //Test(Then)
        assertTrue("No tiene que traer ningun numero por que no hay aulas!",
                classroomRepository.getAllClassroomsNumbers().isEmpty());
    }

    @Test
    public void ifHaveClassroomInTheDataBaseTheirNumbersAreRetrieved(){
        //Setup(Given)
        classroomRepository.saveAll(List.of(
                ClassroomBuilder.buildAClassroom().withName("CyT-1").build(),
                ClassroomBuilder.buildAClassroom().withName("52").build()
        ));

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
        assertEquals("212", classroomRetrieved.map(Classroom::getNumber).orElseGet(()->""),"No recupero el aula dek numero 212");
        assertEquals(aula212.getId(), classroomRetrieved.map(Classroom::getId).orElseGet(()->-1),"No es el mismo aula 212 que se salvo!");
    }

    @Test
    public void shouldNotBePresentIfRetrieveANonExistentClassroomInDB(){
        //Start (Given)

        //Exercise (When)
        Optional<Classroom> classroomRetrieved = classroomRepository.findClassroomsByNumberEquals("212");

        //Test (Then)
        assertFalse("No tiene que recuperar nada por que no hay aula!", classroomRetrieved.isPresent());
        assertEquals("", classroomRetrieved.map(Classroom::getNumber).orElseGet(()->""),"No tiene que haber id por que no hay aula");
    }

    @Test
    public void ifFindByClassroomNumberOfTwoClassroom_OnlyTheseAreRetrieved() {
        //Setup(Given)
        Classroom aClassroom = ClassroomBuilder.buildAClassroom().withName("12").build();
        Classroom aClassroom1 = ClassroomBuilder.buildAClassroom().withName("52").build();
        Classroom aClassroom2 = ClassroomBuilder.buildAClassroom().withName("55").build();
        Classroom aClassroom3 = ClassroomBuilder.buildAClassroom().withName("68").build();
        Classroom aClassroom4 = ClassroomBuilder.buildAClassroom().withName("76").build();
        classroomRepository.saveAll(List.of(aClassroom,aClassroom1,aClassroom2,aClassroom3,aClassroom4));

        //Exercise(When)
        List<Classroom> classroomRetrieved = classroomRepository.findAllByNumberInOrderByNumberAsc(List.of("55","76"));

        //Test(Then)
        assertEquals(2, classroomRetrieved.size());
        assertEquals("55", classroomRetrieved.get(0).getNumber());
        assertEquals("76", classroomRetrieved.get(1).getNumber());
    }
}

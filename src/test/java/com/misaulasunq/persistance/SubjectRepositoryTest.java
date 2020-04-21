package com.misaulasunq.persistance;

import com.misaulasunq.model.*;
import com.misaulasunq.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
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
    public void ifHaveSubjectsWithNameLikeBase_TheirAreRetrievedWhenSerSubjectLike(){
        //Setup(Given)
        //Degree
        Degree testDegree = DegreeBuilder.buildADegree().withMockData().build();

        //Subjects
        Subject matematica = SubjectBuilder.buildASubject().withName("Desarrollo de Aplicaciones")
                .withSubjectCode("33")
                .withDegrees(new ArrayList<>(List.of(testDegree)))
                .build();

        Subject pooI = SubjectBuilder.buildASubject().withName("Base De Datos")
                .withSubjectCode("153")
                .withDegrees(new ArrayList<>(List.of(testDegree)))
                .build();
        Subject pooII = SubjectBuilder.buildASubject().withName("Base de Datos II")
                .withSubjectCode("333")
                .withDegrees(new ArrayList<>(List.of(testDegree)))
                .build();
        testDegree.addSubject(pooI);
        testDegree.addSubject(pooII);
        testDegree.addSubject(matematica);

        subjectRepository.saveAll(List.of(matematica,pooI,pooII));

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
}

package com.misaulasunq.service;


import com.misaulasunq.RestServiceApplication;
import com.misaulasunq.model.Subject;
import com.misaulasunq.services.SubjectService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestServiceApplication.class)
public class SubjectRepositoryTest {

    @Autowired
    private SubjectService subjectService;

//    @Before // FIXME esto está bien o se puede sacar?
//    public void setUp(){
//        subjectService.deleteAll();
//    }

    private Subject getSubject(){ return new Subject(); }

    @Test
    public void whenSaveSubjectFirstTimeIdShoueldBeOne(){
        subjectService.saveSubject(getSubject());
        Subject savedSubject = subjectService.findSubjectById(1);
        assertEquals(Optional.of(1),Optional.of(savedSubject.getId()));
    }

    @Test
    public void whenSaveSeveralSubjectInDbFindAllShouldRetrieveThisEntities(){
        Subject sub1 = getSubject();
        sub1.setName("pf");
        Subject sub2 = new Subject();
        sub2.setName("oop1");
        Subject sub3 = new Subject();
        sub3.setName("oop2");

        subjectService.saveSubject(sub1);
        subjectService.saveSubject(sub2);
        subjectService.saveSubject(sub3);

        List<String> expectedNamesSubjectRetrieved = Arrays.asList("pf","oop1","oop2");
        List<String> subjectNamesRetrieved = subjectService.getAll().stream().map(sub -> sub.getName()).collect(Collectors.toList());

        assertEquals(expectedNamesSubjectRetrieved,subjectNamesRetrieved);
    }
}

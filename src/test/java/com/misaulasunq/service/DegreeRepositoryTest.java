package com.misaulasunq.service;

import com.misaulasunq.RestServiceApplication;
import com.misaulasunq.model.Degree;
import com.misaulasunq.model.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestServiceApplication.class)
public class DegreeRepositoryTest {

    @Autowired
    private DegreeRepository degreeRepository;

    private Subject getSubject() {
        Subject subject = new Subject();
        subject.setName("inpr");
        return subject;
    }

    private Degree getDegree() {
        Degree degree = new Degree();
        degree.setName("tpi");
        degree.setSubjects(Set.of(getSubject()));
        return degree;
    }

    @Test
    public void test(){
    }
}

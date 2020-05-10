package com.misaulasunq.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


@RunWith(SpringRunner.class)
public class SubjectTests {

    private Subject subject;
    private ValidatorFactory factory;
    private Validator validator;
    @Before
    public void setUp() {
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void whenCreatingSubjectWithBlankNameShouldRaiseExceptionThatNameIsMandatory() {

        // given
        Subject subject = new Subject();
        subject.setName("");

        // when
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // then
        ConstraintViolation<Subject> subjectViolations = violations.iterator().next();
        Assert.assertEquals("Name is mandatory", subjectViolations.getMessage());
    }

    @Test
    public void  whenCreatingSubjectWithNoSubjectCodeShouldRaiseExceptionSubjectIsMandatory(){
        // given
        Subject subject = new Subject();
        subject.setName("bioteclogia");
        subject.setSubjectCode("");

        // when
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // then
        ConstraintViolation<Subject> subjectViolations = violations.iterator().next();
        Assert.assertEquals("Subject is mandatory",subjectViolations.getMessage());
    }

    @Test
    public void whenCreatingSubjectCommissionsShouldRaiseEceptionYouNeedToAddAtLeastACommission(){
        // given
        Subject subject = new Subject();
        subject.setName("tpi");
        subject.setSubjectCode("tpi001");
        subject.setCommissions(null);

        // when
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // then
        ConstraintViolation<Subject> subjectViolations = violations.iterator().next();
        Assert.assertEquals("You need to add a commission at least", subjectViolations.getMessage());

    }

    @Test
    public  void whenCreatingSubjectWithNoDegreeShoueldRaiseExpcetionYouNeedToAddADegreeAtLesat() {

        // given
        Subject subject = new Subject();
        subject.setName("tpi");
        subject.setSubjectCode("tpi001");
        subject.setDegrees(null);

        // when
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // then
        ConstraintViolation<Subject> subjectViolations = violations.iterator().next();
        Assert.assertEquals("You need to add a degree at least", subjectViolations.getMessage());
    }



}

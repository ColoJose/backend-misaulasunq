package com.misaulasunq.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


@RunWith(SpringRunner.class)
public class SubjectTests {

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
        subject.setSubjectCode("code");
        subject.addCommission(Mockito.any(Commission.class));
        subject.addDegree(Mockito.any(Degree.class));
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
        subject.setName("name");
        subject.addCommission(Mockito.any(Commission.class));
        subject.addDegree(Mockito.any(Degree.class));
        subject.setSubjectCode("");

        // when
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // then
        ConstraintViolation<Subject> subjectViolations = violations.iterator().next();
        Assert.assertEquals("Subject is mandatory",subjectViolations.getMessage());
    }

    @Test
    public void whenCreatingSubjectWithNoCommissionShouldRaiseExceptionYouNeedToAddACommissionAtLeast(){
        // given
        Subject subject = new Subject();
        subject.setName("name");
        subject.setSubjectCode("code");
        subject.addDegree(Mockito.any(Degree.class));
        // no commission is added

        // when
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // then
        ConstraintViolation<Subject> subjectViolations = violations.iterator().next();
        Assert.assertEquals("You need to add a commission at least", subjectViolations.getMessage());

    }

    @Test
    public  void whenCreatingSubjectWithNoDegreeShouldRaiseExceptionYouNeedToAddADegreeAtLeast() {

        // given
        Subject subject = new Subject();
        subject.setName("tpi");
        subject.setSubjectCode("code");
        subject.addCommission(Mockito.any(Commission.class));

        // when
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // then
        ConstraintViolation<Subject> subjectViolations = violations.iterator().next();
        Assert.assertEquals("You need to add a degree at least", subjectViolations.getMessage());
    }



}

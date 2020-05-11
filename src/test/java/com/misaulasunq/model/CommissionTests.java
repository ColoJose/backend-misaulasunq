package com.misaulasunq.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@RunWith(SpringRunner.class)
public class CommissionTests {

    private Commission commission;
    private ValidatorFactory factory;
    private Validator validator;
    @Before
    public void setUp() {
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }
    @Test
    public void whenCreatingCommissionWithNoNameShouldRaiseExpNameIsMandatory() {
        // given
        this.commission = new Commission();
        this.commission.setYear(Mockito.anyInt());
        commission.setSemester(Semester.PRIMER);
        commission.addSchedule(Mockito.any(Schedule.class));
        commission.setName("");

        // when
        Set<ConstraintViolation<Commission>> violations = this.validator.validate(commission);

        // then
        ConstraintViolation<Commission> commissionViolations = violations.iterator().next();
        Assert.assertEquals("Name Is Mandatory", commissionViolations.getMessage());

    }
    @Test
    public void whenCreatingCommissionWithNoYearShouldRaiseExcpYearIsMandatory() {
        // given
        this.commission = new Commission();
        commission.setSemester(Semester.PRIMER);
        commission.setName("comision 1");
        commission.addSchedule(Mockito.any(Schedule.class));

        // when
        Set<ConstraintViolation<Commission>> violations = this.validator.validate(commission);

        // then
        ConstraintViolation<Commission> commissionViolations = violations.iterator().next();
        Assert.assertEquals("The Year Is Mandatory", commissionViolations.getMessage());
    }

    @Test
    public void whenCreatingCommissionWithNoSemesterShouldRaiseExcpSemesterShouldBePutIt() {
        // given
        this.commission = new Commission();
        this.commission.setName("comison 1");
        this.commission.setYear(Mockito.anyInt());
        this.commission.addSchedule(Mockito.any(Schedule.class));

        // when
        Set<ConstraintViolation<Commission>> violations = this.validator.validate(commission);

        // then
        ConstraintViolation<Commission> commissionViolations = violations.iterator().next();
        Assert.assertEquals("The Semester Should Be Put It",commissionViolations.getMessage());
    }
    @Test
    public void whenCreatingCommissionWithNoSchedulesShouldRaiseEcpSchedulesShouldBeSet() {
        // given
        this.commission = new Commission();
        commission.setSemester(Semester.PRIMER);
        commission.setName("comision 1");
        commission.setYear(2020);

        // when
        Set<ConstraintViolation<Commission>> violations = this.validator.validate(commission);

        // then
        ConstraintViolation<Commission> commissionViolations = violations.iterator().next();
        Assert.assertEquals("You have to add a schedule at least", commissionViolations.getMessage());
    }
}

package com.misaulasunq.model;

import com.misaulasunq.utils.CommissionBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalTime;
import java.util.Set;

@RunWith(SpringRunner.class)
public class ScheduleTests {

    private Validator validator;
    private Schedule schedule;

    @Before
    public void setUp(){
        ValidatorFactory validatorFactory;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Test
    public void whenCreatingScheduleWithNoStartTimeShouldRaiseStartTimeShouldBeSetted() {
        // given
        schedule = new Schedule();
        schedule.setEndTime(LocalTime.now());
        schedule.setDay(Day.LUNES);
        schedule.setCommission(CommissionBuilder.buildACommission().build());

        // when
        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);

        // then
        ConstraintViolation<Schedule> violationSchedule = violations.iterator().next();
        Assert.assertEquals("A Start Time Should Be Setted", violationSchedule.getMessage());
    }

    @Test
    public void whenCreatingScheduleWithNoEndTimeShouldRaiseEnTimeShouldBeSet() {
        // given
        schedule = new Schedule();
        schedule.setDay(Day.LUNES);
        schedule.setStartTime(LocalTime.now());
        schedule.setCommission(CommissionBuilder.buildACommission().build());

        // when
        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);

        // then
        ConstraintViolation<Schedule> violationSchedule = violations.iterator().next();
        Assert.assertEquals("A End Time Should Be Setted", violationSchedule.getMessage());

    }

    @Test
    public void whenCreatingScheduleWithNoWeekDayShouldRaiseDayShouldBePutIt() {
        // given
        schedule = new Schedule();
        schedule.setEndTime(LocalTime.now());
        schedule.setStartTime(LocalTime.now());
        schedule.setCommission(CommissionBuilder.buildACommission().build());

        // when
        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);


        // then
        ConstraintViolation<Schedule> violationSchedule = violations.iterator().next();
        Assert.assertEquals("A Day Should Be Put It", violationSchedule.getMessage());

    }
}

package com.misaulasunq.model;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@RunWith(SpringRunner.class)
public class ScheduleTests {

    @Before
    public void SetUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
    }

    @Test
    public void whenCreatingScheduleWithNoStartTimeShouldRaiseStartTimeShouldBeSetted() {

    }

    @Test
    public void whenCreatingScheduleWithNoEndTimeShouldRaiseEnTimeShouldBeSetted() {

    }

    @Test
    public void whenCreatingScheduleWithNoWeekDayShouldRaiseDayShouldBePutIt() {

    }
}

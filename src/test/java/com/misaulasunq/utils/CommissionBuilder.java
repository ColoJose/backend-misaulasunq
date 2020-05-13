package com.misaulasunq.utils;

import com.misaulasunq.model.*;

import java.time.LocalDate;
import java.util.List;

public class CommissionBuilder {

    private final Commission commission;

    private CommissionBuilder(){
        this.commission  = new Commission();
    }

    public static CommissionBuilder buildACommission(){
        return new CommissionBuilder();
    }

    public CommissionBuilder withMockData(){
        this.commission.setName("No Sense Commission");
        this.commission.setSemester(Semester.ANUAL);
        this.commission.setYear(LocalDate.now().getYear());
        return this;
    }

    public CommissionBuilder withSchedules(List<Schedule> schedules) {
        this.commission.setSchedules(schedules);
        return this;
    }

    public CommissionBuilder withSubject(Subject subject) {
        this.commission.setSubject(subject);
        return this;
    }

    public Commission build(){
        return this.commission;
    }
}

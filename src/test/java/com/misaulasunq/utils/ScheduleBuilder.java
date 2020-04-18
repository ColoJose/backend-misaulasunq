package com.misaulasunq.utils;

import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.Day;
import com.misaulasunq.model.Schedule;
import java.time.LocalTime;

public class ScheduleBuilder {

    private final Schedule schedule;

    private ScheduleBuilder(){
        this.schedule  = new Schedule();
    }

    public static ScheduleBuilder buildASchedule(){
        return new ScheduleBuilder();
    }

    public ScheduleBuilder withMockData(){
        this.schedule.setDay(Day.LUNES);
        this.schedule.setStartTime(LocalTime.of(12,0));
        this.schedule.setEndTime(LocalTime.of(15,0));
        return this;
    }

    public ScheduleBuilder withClassroom(Classroom classroom) {
        this.schedule.setClassroom(classroom);
        return this;
    }

    public Schedule build(){
        return this.schedule;
    }
}

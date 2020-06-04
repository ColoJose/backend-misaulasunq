package com.misaulasunq.utils;

import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.Schedule;
import java.util.List;

public class ClassroomBuilder {

    private final Classroom classroom;

    private ClassroomBuilder(){
        this.classroom  = new Classroom();
    }

    public static ClassroomBuilder buildAClassroom(){
        return new ClassroomBuilder();
    }

    public ClassroomBuilder withName(String number){
        this.classroom.setNumber(number);
        return this;
    }

    public ClassroomBuilder withSchedules(List<Schedule> schedules){
        this.classroom.setSchedules(schedules);
        return this;
    }

    public Classroom build(){
        return this.classroom;
    }
}

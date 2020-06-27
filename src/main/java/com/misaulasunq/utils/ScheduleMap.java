package com.misaulasunq.utils;


import com.misaulasunq.controller.dto.ScheduleDTO;
import com.misaulasunq.exceptions.InvalidDayException;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.Schedule;

import java.util.Map;

public class ScheduleMap {

    private Schedule schedule;
    private ScheduleDTO scheduleDTO;
    private Map<String, Classroom> clasroomMap;

    public ScheduleMap(Schedule schedule, ScheduleDTO scheduleDTO, Map<String, Classroom> clasroomMap) {
        this.schedule    = schedule;
        this.scheduleDTO = scheduleDTO;
        this.clasroomMap = clasroomMap;
    }

    public void update() throws InvalidDayException {
        schedule.setEndTime(scheduleDTO.getEndTime());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setDay(DayConverter.stringToDay(scheduleDTO.getDay()));
        // TODO update del classroom
    }
}

package com.misaulasunq.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.misaulasunq.model.Commission;
import com.misaulasunq.model.Schedule;

import java.util.*;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommissionDTO {

    protected Integer id;
    protected String name;
    protected String semester;
    protected Integer year;
    protected List<ScheduleDTO> schedules;

    public CommissionDTO(Commission commission) {
        this.id = commission.getId();
        this.name = commission.getName();
        this.schedules = this.createScheduleDTO(commission.getSchedules());
        this.semester = commission.getSemester().getSemester();
        this.year = commission.getYear();
    }

    private List<ScheduleDTO> createScheduleDTO(List<Schedule> schedules) {
        return schedules.stream().map(sc -> new ScheduleDTO(sc)).collect(Collectors.toList());
    }

    public Integer getId() {    return id;  }
    public String getName() {   return name;    }
    public List<ScheduleDTO> getSchedules() {    return schedules;   }
    public String getSemester() {   return semester;    }
    public Integer getYear() {  return year;    }
}

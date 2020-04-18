package com.misaulasunq.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.Schedule;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleDTO {

    protected Integer id;
    protected LocalTime startTime;
    protected LocalTime endTime;
    protected ClassroomDTO classroom;
    protected String day;

    public ScheduleDTO(Schedule sc) {
        this.id = sc.getId();
        this.startTime = sc.getStartTime();
        this.endTime = sc.getEndTime();
        this.day = sc.getDay().getDay();
        this.classroom = this.createClassroomDTO(sc.getClassroom());
    }

    private ClassroomDTO createClassroomDTO(Classroom classroom) {
        return new ClassroomDTO(classroom);
    }

    public Integer getId() {    return id;  }
    public LocalTime getStartTime() {   return startTime;   }
    public LocalTime getEndTime() { return endTime; }
    public ClassroomDTO getClassroom() {    return classroom;   }
    public String getDay() {    return day; }
}

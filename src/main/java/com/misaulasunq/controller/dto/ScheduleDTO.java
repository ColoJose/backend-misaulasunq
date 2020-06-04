package com.misaulasunq.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.OverlapNotice;
import com.misaulasunq.model.Schedule;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleDTO {

    protected Integer id;
    protected LocalTime startTime;
    protected LocalTime endTime;
    protected ClassroomDTO classroom;
    protected String day;
    protected List<OverlapNoticeDTO> notices;

    public ScheduleDTO(Schedule sc) {
        this.id = sc.getId();
        this.startTime = sc.getStartTime();
        this.endTime = sc.getEndTime();
        this.day = sc.getDay().getDay();
        this.classroom = this.createClassroomDTO(sc.getClassroom());
        this.notices = this.createOverlapNoticeDTO(sc.getNotices());
    }

    public ScheduleDTO() {};

    private ClassroomDTO createClassroomDTO(Classroom classroom) {
        return new ClassroomDTO(classroom);
    }

    private List<OverlapNoticeDTO> createOverlapNoticeDTO(List<OverlapNotice> notices) {
        return notices.stream().map(OverlapNoticeDTO::new).collect(Collectors.toList());
    }

    public Integer getId() {    return id;  }
    public LocalTime getStartTime() {   return startTime;   }
    public LocalTime getEndTime() { return endTime; }
    public ClassroomDTO getClassroom() {    return classroom;   }
    public String getDay() {    return day; }
    public List<OverlapNoticeDTO> getNotices() {    return notices; }
}

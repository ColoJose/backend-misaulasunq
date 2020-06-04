package com.misaulasunq.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.OverlapNotice;
import com.misaulasunq.model.Schedule;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OverlapNoticeDTO {

    protected Integer id;
    protected String observation;
    protected Integer scheduleConflcitedId;

    public OverlapNoticeDTO(OverlapNotice overlapNotice) {
        this.id = overlapNotice.getId();
        this.observation = overlapNotice.getObservation();
        this.scheduleConflcitedId = overlapNotice.getScheduleConflcited().getId();
    }

    public Integer getId() {    return id;  }
    public String getObservation() {    return observation; }
    public Integer getScheduleConflcitedId() {  return scheduleConflcitedId;    }
}

package com.misaulasunq.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.misaulasunq.model.Classroom;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassroomDTO {

    protected Integer id;
    protected String number;

    public ClassroomDTO() {}

    public ClassroomDTO(Classroom classroom) {
        this.id = classroom.getId();
        this.number = classroom.getNumber();
    }

    public Integer getId() {    return id;  }
    public String getNumber() { return number;  }
}

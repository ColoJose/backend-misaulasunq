package com.misaulasunq.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.misaulasunq.model.Classroom;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassroomDTO {

    protected Integer id;
    protected String number;
    protected String imageUrl;

    public ClassroomDTO(Classroom classroom) {
        this.id = classroom.getId();
        this.number = classroom.getNumber();
        this.imageUrl = classroom.getImageClassRoomBase64();
    }

    public Integer getId() {    return id;  }
    public String getNumber() { return number;  }
    public String getImageUrl() {   return imageUrl;    }
}

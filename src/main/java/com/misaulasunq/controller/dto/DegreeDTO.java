package com.misaulasunq.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.misaulasunq.model.Degree;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DegreeDTO {

    protected Integer id;
    protected String name;

    public DegreeDTO(Degree dg) {
        this.id = dg.getId();
        this.name = dg.getName();
    }

    public Integer getId() {    return id;  }
    public void setId(Integer id) { this.id = id;   }

    public String getName() {   return name;    }
    public void setName(String name) {  this.name = name;   }
}

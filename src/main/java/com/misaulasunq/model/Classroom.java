package com.misaulasunq.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Classroom {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Classroom Number Is Mandatory")
    @Column(unique = true, length = 10)
    private String number;
    private String imageClassRoomBase64; //TODO: Se podria hacer una clase y tabla para representar la imagen y guardarla.
    @NotNull(message = "Schedules Cannot Be A Null Variable")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "classroom")
    private List<Schedule> schedules;

    public Classroom() {   this.initialize();  }

    private void initialize() {
        this.number = "";
        this.imageClassRoomBase64 = "";
        this.schedules = new ArrayList<>();
    }

    public Integer getId() {    return id;  }
    public void setId(Integer id) { this.id = id;   }

    public String getNumber() { return number;  }
    public void setNumber(String number) {  this.number = number;   }

    public String getImageClassRoomBase64() {   return imageClassRoomBase64;    }
    public void setImageClassRoomBase64(String imageClassRoomBase64) {
        this.imageClassRoomBase64 = imageClassRoomBase64;
    }

    public List<Schedule> getSchedules() {  return schedules;   }
    public void setSchedules(List<Schedule> schedules) {    this.schedules = schedules; }
}

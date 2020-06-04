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
    @NotNull(message = "Schedules Cannot Be A Null Variable")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "classroom")
    private List<Schedule> schedules;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "classroom")
    private List<OverlapNotice> notices;

    public Classroom() {   this.initialize();  }

    private void initialize() {
        this.number = "";
        this.schedules = new ArrayList<>();
        this.notices = new ArrayList<>();
    }

    public void addSchedule(Schedule schedule) {
        this.getSchedules().add(schedule);
    }

    public Integer getId() {    return id;  }
    public void setId(Integer id) { this.id = id;   }

    public String getNumber() { return number;  }
    public void setNumber(String number) {  this.number = number;   }

    public List<Schedule> getSchedules() {  return schedules;   }
    public void setSchedules(List<Schedule> schedules) {    this.schedules = schedules; }

    public List<OverlapNotice> getNotice() {  return notices; }
    public void setNotice(List<OverlapNotice> notices) {  this.notices = notices; }
}

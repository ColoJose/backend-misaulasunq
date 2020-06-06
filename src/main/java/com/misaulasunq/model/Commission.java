package com.misaulasunq.model;

import com.misaulasunq.controller.dto.CommissionDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Commission {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name Is Mandatory")
    private String name;
    @NotNull(message = "The Year Is Mandatory")
    private Integer year;
    @NotNull(message = "The Semester Should Be Put It")
    @Enumerated(EnumType.STRING)
    private Semester semester;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Subject subject;
    @Size(min= 1, message = "You have to add a schedule at least")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "commission")
    private List<Schedule> schedules;

    public Commission() {   this.initialize();  }

    private void initialize() {
        this.schedules = new ArrayList<>();
    }

    public void addSchedule(Schedule aSchudule) {
        this.getSchedules().add(aSchudule);
    }

    public Integer getId() {    return id;  }
    public void setId(Integer id) { this.id = id;   }

    public String getName() {   return name;    }
    public void setName(String name) {  this.name = name;   }

    public Subject getSubject() {   return subject; }
    public void setSubject(Subject subject) {   this.subject = subject; }

    public List<Schedule> getSchedules() {   return schedules;   }
    public void setSchedules(List<Schedule> schedules) { this.schedules = schedules; }

    public Integer getYear() {  return year;    }
    public void setYear(Integer year) { this.year = year;   }

    public Semester getSemester() { return semester;    }
    public void setSemester(Semester semester) {    this.semester = semester;   }
}

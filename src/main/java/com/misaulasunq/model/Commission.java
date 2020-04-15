package com.misaulasunq.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/** OBS: Se deberia agregar un campo para referenciar a que cuatrimestre corresponde (Enum primer/segundo/anual) y un campo para guardar a que año corresponde*/
@Entity
public class Commission {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name Is Mandatory")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Subject subject;
    @NotNull(message = "Schedules Cannot Be A Null Variable")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,/* orphanRemoval = true, */mappedBy = "commission")
    private Set<Schedule> schedules;

    public Commission() {   this.initialize();  }

    private void initialize() {
        this.name = "";
        this.schedules = new HashSet<>();
    }

    public Integer getId() {    return id;  }
    public void setId(Integer id) { this.id = id;   }

    public String getName() {   return name;    }
    public void setName(String name) {  this.name = name;   }

    public Subject getSubject() {   return subject; }
    public void setSubject(Subject subject) {   this.subject = subject; }

    public Set<Schedule> getSchedules() {   return schedules;   }
    public void setSchedules(Set<Schedule> schedules) { this.schedules = schedules; }
}

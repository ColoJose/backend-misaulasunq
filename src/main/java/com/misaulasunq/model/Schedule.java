package com.misaulasunq.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@Entity(name ="schedule")
public class Schedule {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    @NotNull(message = "Commission Cannot Be A Null Variable")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Commission commission;
    @NotNull(message = "A Start Time Should Be Setted")
    private LocalTime startTime;
    @NotNull(message = "A End Time Should Be Setted")
    private LocalTime endTime;
//    @NotNull(message = "Classroom Cannot Be A Null Variable")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Classroom classroom;

}

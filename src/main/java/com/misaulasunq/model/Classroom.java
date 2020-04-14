package com.misaulasunq.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
}

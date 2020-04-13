package com.misaulasunq.model;

import javax.persistence.*;
import java.time.LocalTime;

@Entity(name ="schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Commission commission;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer classRoomNumber;
    private String imageClassRoomBase64;

}

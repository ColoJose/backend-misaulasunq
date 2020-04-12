package com.misaulasunq.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalTime;

@Entity(name ="schedule")
public class Schedule {

    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Commission commission;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer classRoomNumber;
    private String imageClassRoomBase64;

}

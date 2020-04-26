package com.misaulasunq.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatter;

@Entity(name ="schedule")
public class Schedule {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Commission commission;
    @NotNull(message = "A Start Time Should Be Setted")
//    @Column(columnDefinition = "TIME")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME,pattern = "%H:mm:ss")
    private LocalTime startTime;
    @NotNull(message = "A End Time Should Be Setted")
//    @Column(columnDefinition = "TIME")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "%H:mm:ss")
    private LocalTime endTime;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Classroom classroom;
    @NotNull(message = "A Day Should Be Put It")
    @Enumerated(EnumType.STRING)
    private Day day;

    public Schedule() { }

    public Integer getId() {    return id;  }
    public void setId(Integer id) { this.id = id;   }

    public Commission getCommission() { return commission;  }
    public void setCommission(Commission commission) {  this.commission = commission;   }

    public LocalTime getStartTime() {   return startTime;   }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;/* LocalDateTime.of(LocalDate.now(),startTime); */}

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime;/*LocalDateTime.of(LocalDate.now(),endTime); */}

    public Classroom getClassroom() {   return classroom;   }
    public void setClassroom(Classroom classroom) { this.classroom = classroom; }

    public Day getDay() {   return day; }
    public void setDay(Day day) {   this.day = day; }
}

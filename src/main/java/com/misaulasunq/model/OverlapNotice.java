package com.misaulasunq.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity()
public class OverlapNotice {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    @NotBlank(message = "You Should Put An Observation")
    private final String observation = "El horario se encuentra super puesto para el aula y día asignados.";
    @NotNull(message = "Should Be assign an schedule")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Schedule scheduleAffected;
    @NotNull(message = "Should Be assign the schedule affected")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Schedule scheduleConflcited;
    @NotNull(message = "Should Be assign the schedule in conflict")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Classroom classroom;

    public OverlapNotice() {    }

    public static OverlapNotice makeOverlapNotice(Schedule scheduleAffected, Schedule scheduleConflcited){
        OverlapNotice notice = new OverlapNotice();
        notice.setClassroom(scheduleAffected.getClassroom());
        notice.setScheduleAffected(scheduleAffected);
        notice.setScheduleConflcited(scheduleConflcited);
        scheduleAffected.addOverlapNotice(notice);
        scheduleConflcited.addOverlapNotice(notice);
        return notice;
    }


    public Integer getId() { return id;  }
    public void setId(Integer id) {  this.id = id;   }
    public String getObservation() {    return observation; }
    public Classroom getClassroom() {   return classroom;   }
    public void setClassroom(Classroom classroom) { this.classroom = classroom; }
    public Schedule getScheduleAffected() { return scheduleAffected;    }
    public void setScheduleAffected(Schedule scheduleAffected) {    this.scheduleAffected = scheduleAffected;   }
    public Schedule getScheduleConflcited() {   return scheduleConflcited;  }
    public void setScheduleConflcited(Schedule scheduleConflcited) {    this.scheduleConflcited = scheduleConflcited;   }
}

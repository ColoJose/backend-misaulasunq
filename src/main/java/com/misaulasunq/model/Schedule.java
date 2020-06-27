package com.misaulasunq.model;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name ="schedule")
public class Schedule {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "A Comission Should Be Setted")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Commission commission;
    @NotNull(message = "A Start Time Should Be Setted")
    private LocalTime startTime;
    @NotNull(message = "A End Time Should Be Setted")
    private LocalTime endTime;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Classroom classroom;
    @NotNull(message = "A Day Should Be Put It")
    @Enumerated(EnumType.STRING)
    private Day day;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "scheduleAffected")
    private List<OverlapNotice> notices;

    public Schedule() { this.initialize();   }

    private void initialize(){
        this.notices = new ArrayList<>();
    }

    public void addOverlapNotice(OverlapNotice overlapNotice) {
        this.getNotices().add(overlapNotice);
    }

    public Integer getId() {    return id;  }
    public void setId(Integer id) { this.id = id;   }

    public Commission getCommission() { return commission;  }
    public void setCommission(Commission commission) {  this.commission = commission;   }

    public LocalTime getStartTime() {   return startTime;   }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public Classroom getClassroom() {   return classroom;   }
    public void setClassroom(Classroom classroom) { this.classroom = classroom; }

    public Day getDay() {   return day; }
    public void setDay(Day day) {   this.day = day; }

    public List<OverlapNotice> getNotices() {  return notices; }
    public void setNotices(List<OverlapNotice> notices) {  this.notices = notices; }
}

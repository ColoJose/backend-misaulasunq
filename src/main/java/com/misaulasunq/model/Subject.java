package com.misaulasunq.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity(name = "subject") // name when using HQL
public class Subject {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Subject is mandatory")
    @Column(unique = true, length = 10)
    private String subjectCode; // to avoid duplicate subjects
    @Size(min = 1,message = "You need to add a commission at least")
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subject")
    private List<Commission> commissions;
    @Size(min=1,message = "You need to add a degree at least")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Degree> degrees;

    public Subject() {  this.initialize();  }

    protected void initialize(){
        this.commissions = new ArrayList<>();
        this.degrees = new ArrayList<>();
    }

    public void addCommission(Commission commission) {
        this.getCommissions().add(commission);
    }

    public void addCommissions(List<Commission> commissions) {
        commissions.addAll(commissions);
    }

    public void addDegree(Degree aDegree) { this.getDegrees().add(aDegree); }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Degree> getDegrees() { return degrees; }
    public void setDegrees(List<Degree> degrees) { this.degrees = degrees; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public List<Commission> getCommissions() { return commissions; }
    public void setCommissions(List<Commission> commissions) { this.commissions = commissions; }
}

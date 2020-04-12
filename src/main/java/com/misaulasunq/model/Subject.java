package com.misaulasunq.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity(name = "subject") // name when using HQL
public class Subject {

    @Id
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String subjectCode; // to avoid duplicate subjects
    @OneToMany(orphanRemoval = true)
    private Set<Commission> commissions;
    @ManyToMany(mappedBy = "subjects")
    private Set<Degree> degrees;

    public Subject() {}

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Collection<Degree> getDegrees() { return degrees; }

    public void setDegrees(Set<Degree> degrees) { this.degrees = degrees; }

    public String getSubjectCode() { return subjectCode; }

    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public Set<Commission> getCommissions() { return commissions; }

    public void setCommissions(Set<Commission> commissions) { this.commissions = commissions; }
}

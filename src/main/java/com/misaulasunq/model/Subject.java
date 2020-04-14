package com.misaulasunq.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "subject") // name when using HQL
public class Subject {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name Is Mandatory")
    private String name;
    @NotBlank(message = "Subject Is Mandatory")
    @Column(unique = true, length = 10)
    private String subjectCode; // to avoid duplicate subjects
    @NotNull(message = "Commissions Cannot Be A Null Variable")
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subject")
    private Set<Commission> commissions;
    @NotNull(message = "Degrees Cannot Be A Null Variable")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Degree> degrees;

    public Subject() {
        this.initialize();
    }

    protected void initialize(){
        this.name = "";
        this.subjectCode = "";
        this.commissions = new HashSet<Commission>();
        this.degrees = new HashSet<Degree>();
    }

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

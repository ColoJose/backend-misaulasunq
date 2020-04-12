package com.misaulasunq.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity(name = "subject") // name when using HQL
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String subjectCode; // to avoid duplicate subjects
    @OneToMany
    private Set<Commission> commissions;
    @ManyToMany(mappedBy = "subjects")
    private Degree degree;

    public Subject() {}

    public Subject(String name){
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "subjects")
    private Collection<Degree> degrees;

    public Collection<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(Collection<Degree> degrees) {
        this.degrees = degrees;
    }
}

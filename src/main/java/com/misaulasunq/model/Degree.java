package com.misaulasunq.model;

import javax.persistence.*;
import java.util.Set;


@Entity(name = "entity")
public class Degree {

    @Id
    @Column(name = "degree_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToMany
    @JoinTable(name="degree_subject",
               joinColumns = @JoinColumn(name= "degree_id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects;

    public Degree() { }

    // getters and setters

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Set<Subject> getSubjects() { return subjects; }

    public void setSubjects(Set<Subject> subjects) { this.subjects = subjects; }
}

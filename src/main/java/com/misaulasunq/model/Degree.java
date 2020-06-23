package com.misaulasunq.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "degree")
public class Degree {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name Is Mandatory")
    private String name;
    @NotBlank(message = "Degree Code Is Mandatory")
    private String code;
    @NotNull(message = "Subjects Cannot Be A Null Variable")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="degree_subject",
               joinColumns = @JoinColumn(name= "degree_id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

    public Degree() {   this.initialize();  }

    public void initialize(){
        this.name ="";
        this.code ="";
        this.subjects = new ArrayList<>();
    }

    public void addSubject(Subject subject) {
        this.getSubjects().add(subject);
    }

    public Integer getId() {    return id;  }
    public void setId(Integer id) { this.id = id;   }

    public String getName() {   return name;    }
    public void setName(String name) {  this.name = name;   }

    public List<Subject> getSubjects() { return subjects;    }
    public void setSubjects(List<Subject> subjects) {    this.subjects = subjects;   }

    public String getCode() {   return code;    }
    public void setCode(String code) {  this.code = code;   }
}

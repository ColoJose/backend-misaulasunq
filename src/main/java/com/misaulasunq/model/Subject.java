package com.misaulasunq.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;// question: ¿se hara unico o se usara algun codigo para evitar duplicidad?
    @Transient //FIXME: se tiene definir el mapeo
    private List<Commission> commissions;
    @Transient //FIXME: tiene que definirse el mapeo
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
}

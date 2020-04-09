package ar.edu.unq.miaulaunq.model;

import java.util.List;

public class Subject {

    private String id;
    private String name; // question: ¿se hara unico o se usara algun codigo para evitar duplicidad?
    private List<Commission> commissions;
    private Degree degree;
}

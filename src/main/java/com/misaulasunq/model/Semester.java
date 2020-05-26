package com.misaulasunq.model;

public enum Semester {

    PRIMER("Primer cuatrimestre"),
    SEGUNDO("Segundo cuatrimestre"),
    ANUAL("Anual");

    private final String semester;

    Semester(String semester) {   this.semester = semester; }
    public String getSemester() {    return semester; }
}

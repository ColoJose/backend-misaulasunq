package com.misaulasunq.model;

public enum Semester {

    PRIMER("Primer"),
    SEGUNDO("Segundo"),
    ANUAL("Anual"),
    ;

    private final String semester;

    Semester(String semester) {   this.semester = semester; }
    public String getSemester() {    return semester; }
}

package com.misaulasunq.model;

public enum Day {

    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miercoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sabado"),
    DOMINGO("Domingo")
    ;

    private final String day;

    Day(String day) {   this.day = day; }

    public String getDay() {    return day; }
}

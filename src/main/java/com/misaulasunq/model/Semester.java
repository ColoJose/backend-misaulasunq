package com.misaulasunq.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public enum Semester {

//        @JsonProperty("Primer cuatrimestre")
//        PRIMER,
//        @JsonProperty("Segundo cuatrimestre")
//        SEGUNDO,
//        @JsonProperty("Anual")
//        ANUAL;

//        private static Map<String,Semester> namesMap = new HashMap<String,Semester>(3);
//
//        static{
//            namesMap.put("Primer cuatrimestre", PRIMER);
//            namesMap.put("Segundo cuatrimestre", SEGUNDO);
//            namesMap.put("Anual", ANUAL);
//        }
//
//        @JsonCreator

    PRIMER("Primer cuatrimestre"),
    SEGUNDO("Segundo cuatrimestre"),
    ANUAL("Anual");

    private final String semester;

    Semester(String semester) {   this.semester = semester; }
    public String getSemester() {    return semester; }
}

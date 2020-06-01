package com.misaulasunq.controller.dto;

public class GeneralInfo {

    private String name;
    private String subjectCode;

    private GeneralInfo () {}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSubjectCode() { return subjectCode; }

    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
}

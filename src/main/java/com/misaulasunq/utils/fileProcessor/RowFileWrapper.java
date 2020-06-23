package com.misaulasunq.utils.fileProcessor;

import com.misaulasunq.model.Day;
import com.misaulasunq.model.Semester;

import java.time.LocalTime;

public class RowFileWrapper {
    private final String degreeCode;
    private final String subjectName;
    private final String subjectCode;
    private final String commissionName;
    private final Semester semster;
    private final Integer year;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Day day;
    private final String classroom;

    public RowFileWrapper(
                String degreeCode,
                String subjectName,
                String subjectCode,
                String commissionName,
                Semester semster,
                Integer year,
                LocalTime startTime,
                LocalTime endTime,
                Day day,
                String classroom
    ) {
        this.degreeCode = degreeCode;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.commissionName = commissionName;
        this.semster = semster;
        this.year = year;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.classroom = classroom;
    }

    public String getDegreeCode() { return degreeCode;  }
    public String getSubjectName() {    return subjectName; }
    public String getSubjectCode() {    return subjectCode; }
    public String getCommissionName() { return commissionName;  }
    public Semester getSemster() {  return semster; }
    public Integer getYear() {  return year;    }
    public LocalTime getStartTime() {   return startTime;   }
    public LocalTime getEndTime() { return endTime; }
    public Day getDay() {   return day; }
    public String getClassroom() {  return classroom;   }
}

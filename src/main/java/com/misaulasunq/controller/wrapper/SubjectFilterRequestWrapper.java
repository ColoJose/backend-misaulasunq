package com.misaulasunq.controller.wrapper;

import com.misaulasunq.model.Day;
import com.misaulasunq.utils.SearchFilter;
import java.time.LocalTime;
import java.util.List;

public class SubjectFilterRequestWrapper {

    private List<SearchFilter> searchFilters;
    private Day day;
    private String classroomNumber;
    private String subjectName;
    private LocalTime startTime;
    private LocalTime endTime;

    public SubjectFilterRequestWrapper(List<SearchFilter> searchFilters, Day day, String classroomNumber, String subjectName, LocalTime startTime, LocalTime endTime) {
        this.searchFilters = searchFilters;
        this.day = day;
        this.classroomNumber = classroomNumber;
        this.subjectName = subjectName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public SubjectFilterRequestWrapper() {}

    public Day getDay() {   return day; }
    public void setDay(Day day) {   this.day = day; }
    public String getClassroomNumber() {    return classroomNumber; }
    public void setClassroomNumber(String classroomNumber) {    this.classroomNumber = classroomNumber; }
    public String getSubjectName() {    return subjectName; }
    public void setSubjectName(String subjectName) {    this.subjectName = subjectName; }
    public LocalTime getStartTime() {   return startTime;   }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public List<SearchFilter> getSearchFilters() {  return searchFilters;   }
    public void setSearchFilters(List<SearchFilter> searchFilters) {    this.searchFilters = searchFilters; }
}

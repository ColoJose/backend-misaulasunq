package com.misaulasunq.controller.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.misaulasunq.exceptions.InvalidDayException;
import com.misaulasunq.model.Day;
import com.misaulasunq.utils.DayConverter;
import com.misaulasunq.utils.SearchFilter;
import net.minidev.json.annotate.JsonIgnore;
import java.time.LocalTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectFilterRequestWrapper {

    protected List<SearchFilter> searchFilters;
    @JsonIgnore protected Day enumDay;
    protected String day;
    protected String classroomNumber;
    protected String subjectName;
    protected LocalTime startTime;
    protected LocalTime endTime;

    public SubjectFilterRequestWrapper(List<SearchFilter> searchFilters, String day, String classroomNumber, String subjectName, LocalTime startTime, LocalTime endTime) {
        this.searchFilters = searchFilters;
        this.day = day;
        this.classroomNumber = classroomNumber;
        this.subjectName = subjectName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public SubjectFilterRequestWrapper() {}

    public void setDay(String day) throws InvalidDayException {
        this.day = day;
        if(!day.isBlank()){
            this.setEnumDay(DayConverter.stringToDay(day));
        }
    }

    public Day getEnumDay() {   return enumDay; }
    public void setEnumDay(Day enumDay) {   this.enumDay = enumDay; }
    public String getDay() {   return day; }
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

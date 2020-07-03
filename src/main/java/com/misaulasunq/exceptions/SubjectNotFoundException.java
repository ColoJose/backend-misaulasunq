package com.misaulasunq.exceptions;

import java.time.LocalTime;

public class SubjectNotFoundException extends Exception {

    public static SubjectNotFoundException SubjectNotFoundByNumber(String classroomnumber){
        return new SubjectNotFoundException(String.format("No subjects in the classroom %s.", classroomnumber));
    }

    public static SubjectNotFoundException SubjectNotFoundByName(String name){
        return new SubjectNotFoundException(String.format("No subjects with the Name %s.", name));
    }

    public static SubjectNotFoundException SubjectNotFoundBetween(LocalTime startTime, LocalTime endtime){
        return new SubjectNotFoundException(String.format("No subjects Between Hours %s - %s.", startTime, endtime));
    }

    public SubjectNotFoundException(Integer id){
        super(String.format("Subject with the id: %s not found", id.toString()));
    }

    public SubjectNotFoundException(String message){
        super(message);
    }

    public static SubjectNotFoundException SubjectNotFoundCurrentDay() {
        return new SubjectNotFoundException(String.format("No subjects in the current day"));
    }

    public static SubjectNotFoundException SubjectNotFoundByCriteria() {
        return new SubjectNotFoundException("No subjects by that criteria.");
    }
}

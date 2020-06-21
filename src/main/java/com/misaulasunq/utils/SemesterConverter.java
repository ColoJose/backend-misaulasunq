package com.misaulasunq.utils;

import com.misaulasunq.exceptions.InvalidSemesterException;
import com.misaulasunq.model.Semester;

public class SemesterConverter {

    public static Semester stringToSemester(String aString) throws InvalidSemesterException {
        Semester aSemester;
        switch (aString.toUpperCase()) {
            case "PRIMER":
                aSemester = Semester.PRIMER;
                break;
            case "SEGUNDO":
                aSemester = Semester.SEGUNDO;
                break;
            case "ANUAL":
                aSemester = Semester.ANUAL;
                break;
            default:
                throw new InvalidSemesterException(aString);
        }
        return aSemester;
    }
}

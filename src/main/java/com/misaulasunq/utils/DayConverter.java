package com.misaulasunq.utils;

import com.misaulasunq.exceptions.InvalidDay;
import com.misaulasunq.exceptions.InvalidSemester;
import com.misaulasunq.model.Day;

import java.time.DayOfWeek;

public final class DayConverter {

    public static Day getDay(DayOfWeek dayOfWeek) {
        Day day = null;
        switch (dayOfWeek) {
            case MONDAY: day = Day.LUNES;
                         break;
            case TUESDAY: day =  Day.MARTES;
                         break;
            case WEDNESDAY: day = Day.MIERCOLES;
                         break;
            case THURSDAY: day = Day.JUEVES;
                         break;
            case FRIDAY: day =  Day.VIERNES;
                         break;
            case SATURDAY: day = Day.SABADO;
                         break;
            default: day = Day.DOMINGO;
                         break;
                
        }
        return day;
    }

    public static Day stringToDay(String stringToConvert) throws InvalidDay {
        Day day;
        switch (stringToConvert.toUpperCase()) {
            case "LUNES":
                day = Day.LUNES;
                break;
            case "MARTES":
                day =  Day.MARTES;
                break;
            case "MIÉRCOLES": case "MIERCOLES":
                day = Day.MIERCOLES;
                break;
            case "JUEVES":
                day = Day.JUEVES;
                break;
            case "VIERNES":
                day =  Day.VIERNES;
                break;
            case "SABADO": case "SÁBADO":
                day = Day.SABADO;
                break;
            default:
                throw new InvalidDay(stringToConvert);
        }
        return day;
    }
}

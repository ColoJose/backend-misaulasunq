package com.misaulasunq.utils;

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
        }
        return day;
    }
}

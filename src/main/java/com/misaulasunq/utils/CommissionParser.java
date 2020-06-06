package com.misaulasunq.utils;

import com.misaulasunq.controller.dto.CommissionDTO;
import com.misaulasunq.controller.dto.ScheduleDTO;
import com.misaulasunq.model.*;

import java.util.List;
import java.util.stream.Collectors;

public  class CommissionParser {

    public static List<Commission> parseCommissions(List<CommissionDTO> commissions, Subject subject) {
        return commissions.stream().map(com -> parseCommission(com, subject))
                .collect(Collectors.toList());
    }

    private static Commission parseCommission(CommissionDTO com, Subject subject) {
        Commission commission = new Commission();
        commission.setName(com.getName());
        commission.setYear(com.getYear());
        commission.setSemester(Semester.valueOf(toEnumSemesterString(com.getSemester())));
        commission.setSubject(subject);
        commission.setSchedules(
                com.getSchedules().stream().map(sch -> parseSchedule(sch)).collect(Collectors.toList())
        );
        return commission;

    }

    private static Schedule parseSchedule(ScheduleDTO sch) {
        Schedule schedule = new Schedule();
        schedule.setDay( Day.valueOf(toDayEnumString(sch.getDay())) );
        schedule.setStartTime(sch.getStartTime());
        schedule.setEndTime(sch.getEndTime());
        Classroom classroom = new Classroom();
        classroom.setNumber("52");
        schedule.setClassroom(classroom);
        return schedule;
    }

    private static String toEnumSemesterString(String semester) {
        String res = new String();
        switch (semester) {
            case "Primer cuatrimestre": res = "PRIMER";
                break;
            case "Segundo cuatrimestre": res = "SEGUNDO";
                break;
            case "Anual": res = "ANUAL";
                break;
        }
        return res;
    }

    private static String toDayEnumString(String day) {
        String dayString = new String();
        switch (day) {
            case "Lunes": dayString = "LUNES";
                break;
            case "Martes": dayString = "MARTES";
                break;
            case "Miércoles": dayString = "MIERCOLES";
                break;
            case "Jueves": dayString = "JUEVES";
                break;
            case "Viernes": dayString = "VIERNES";
                break;
            case "Sábado": dayString = "SABADO";
                break;
        }

        return  dayString;
    }
}

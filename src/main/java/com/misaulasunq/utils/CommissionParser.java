package com.misaulasunq.utils;

import com.misaulasunq.controller.dto.CommissionDTO;
import com.misaulasunq.controller.dto.ScheduleDTO;
import com.misaulasunq.exceptions.ClassroomNotFoundException;
import com.misaulasunq.model.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public  class CommissionParser {

    public List<Commission> parseCommissions(List<CommissionDTO> commissions, Subject subject, Map<String, Classroom> classroomMap)  {
        return commissions.stream().map(com -> parseCommission(com, subject, classroomMap))
                .collect(Collectors.toList());
    }

    private Commission parseCommission(CommissionDTO com, Subject subject, Map<String, Classroom> classroomMap)  {
        Commission commission = new Commission();
        commission.setName(com.getName());
        commission.setYear(com.getYear());
        commission.setSemester(Semester.valueOf(toEnumSemesterString(com.getSemester())));
        commission.setSubject(subject);
        commission.setSchedules(
                com.getSchedules().stream().map(sch -> {
                    try {
                        return parseSchedule(sch, commission, classroomMap);
                    } catch (ClassroomNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList())
        );
        return commission;
    }

    private  Schedule parseSchedule(ScheduleDTO sch, Commission commission,  Map<String, Classroom> classroomMap) throws ClassroomNotFoundException {
        Schedule schedule = new Schedule();
        schedule.setDay( Day.valueOf(toDayEnumString(sch.getDay())) );
        schedule.setStartTime(sch.getStartTime());
        schedule.setEndTime(sch.getEndTime());
        Classroom classroom = classroomMap.get(sch.getClassroom().getNumber());
        schedule.setClassroom(classroom);
        schedule.setCommission(commission);

        return schedule;
    }

    private String toEnumSemesterString(String semester) {
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

    private String toDayEnumString(String day) {
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

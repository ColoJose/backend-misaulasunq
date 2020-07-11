package com.misaulasunq.exceptions;

import com.misaulasunq.model.Day;

import java.time.LocalTime;

public class DuplicateScheduleException extends Exception {
    public DuplicateScheduleException(String name, String classroom, Day day, LocalTime startTime, LocalTime endTime) {
        super(
            String.format(
                "Ya existe para la Comision: %s en el aula %s en el horario %s - %s en el dia %s.",
                name, classroom, startTime, endTime, day
            )
        );
    }
}

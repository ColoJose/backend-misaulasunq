package com.misaulasunq.model;

import com.misaulasunq.utils.ClassroomBuilder;
import com.misaulasunq.utils.ScheduleBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class OverlapNoticeTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void whenMakeAnObservation_TheClassroomAndTheSchedulesInConflictAreLinked() {
        //Setup(given)
        Classroom aClassRoom = ClassroomBuilder.buildAClassroom().withName("55").build();
        Schedule aScheduleAffected = ScheduleBuilder.buildASchedule()
                .withDay(Day.LUNES)
                .withStartTime(LocalTime.of(10,30))
                .withEndTime(LocalTime.of(13,30))
                .withClassroom(aClassRoom)
                .build();
        Schedule aScheduleConflicted = ScheduleBuilder.buildASchedule()
                .withDay(Day.LUNES)
                .withStartTime(LocalTime.of(10,30))
                .withEndTime(LocalTime.of(13,30))
                .withClassroom(aClassRoom)
                .build();

        //Exercise(When)
        OverlapNotice anOverlap = OverlapNotice.makeOverlapNotice(aScheduleAffected, aScheduleConflicted);

        //Test(then)
        assertEquals("El horario se encuentra super puesto para el aula y día asignados.", anOverlap.getObservation());
        assertEquals(aClassRoom, anOverlap.getClassroom());
        assertEquals(aScheduleAffected, anOverlap.getScheduleAffected());
        assertEquals(aScheduleConflicted, anOverlap.getScheduleConflcited());
    }
}
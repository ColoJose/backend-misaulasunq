package com.misaulasunq.service;

import com.misaulasunq.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Integer> {

//    @Query("SELECT classroom "
//         + "FROM Classroom classroom "
//         + "LEFT JOIN classroom.schedules AS schedule "
//         + "WHERE schedule.startTime = :startTime")
//    List<Classroom> findClassroomWithSchedulesStartingAt(@Param("startTime") LocalTime startingTime);

    Optional<Classroom> findClassroomsByNumberEquals(String number);
}

package com.misaulasunq.persistance;

import com.misaulasunq.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

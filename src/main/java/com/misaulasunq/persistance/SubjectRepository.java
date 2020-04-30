package com.misaulasunq.persistance;

import com.misaulasunq.model.Subject;
import com.misaulasunq.utils.DayConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Autowired
    private Subject sub;

    @Query("SELECT subject "
         + "FROM subject subject "
         + "JOIN subject.commissions AS commisions "
         + "JOIN commisions.schedules AS schedules "
         + "JOIN schedules.classroom AS classroom "
         + "WHERE classroom.number = :classroomNumber "
         + "GROUP BY subject")
    List<Subject> findSubjectThatAreInClassroom(@Param("classroomNumber") String classroomNumber);

    List<Subject> findSubjectByName(@Param("name") String name);

    @Query("SELECT subject "
            + "FROM subject subject "
            + "JOIN subject.commissions AS commisions "
            + "JOIN commisions.schedules AS schedule "
            + "WHERE (schedule.startTime BETWEEN :startTime AND :endTime) "
            +   "OR (schedule.endTime BETWEEN :startTime AND :endTime) "
            + "GROUP BY subject")
    List<Subject> findSubjectsBetweenHours(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Query("SELECT subject "
        + "FROM subject subject "
        + "JOIN subject.commissions AS commissions "
        + "JOIN commissions.schedules AS schedule "
        + "WHERE schedule.day=" + DayConverter.
     )
    List<Subject> findCurrentDaySubjects(DayOfWeek currentDay);
}

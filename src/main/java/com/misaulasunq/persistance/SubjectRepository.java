package com.misaulasunq.persistance;

import com.misaulasunq.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query("SELECT subject "
         + "FROM subject subject "
         + "JOIN subject.commissions AS commisions "
         + "JOIN commisions.schedules AS schedules "
         + "JOIN schedules.classroom AS classroom "
         + "WHERE classroom.number = :classroomNumber "
         + "GROUP BY subject"
         )
    List<Subject> findSubjectThatAreInClassroom(@Param("classroomNumber") String classroomNumber);

    List<Subject> findSubjectByName(@Param("name") String name);

    @Query("SELECT subject "
         + "FROM subject subject "
         + "JOIN subject.commissions AS commisions "
         + "JOIN commisions.schedules AS schedules "
         + "WHERE (schedules.startTime >= :startTime "
                + "AND schedules.startTime <= :endTime)"
             + "OR (schedules.endTime >= :startTime "
                + "AND schedules.endTime <= :endTime)"
         + "GROUP BY subject"
    )
    List<Subject> findSubjectsBetweenHours(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}

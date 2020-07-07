package com.misaulasunq.persistance;

import com.misaulasunq.model.Day;
import com.misaulasunq.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    @Deprecated
    @Query("SELECT subject "
         + "FROM subject subject "
         + "JOIN subject.commissions AS commisions "
         + "JOIN commisions.schedules AS schedules "
         + "JOIN schedules.classroom AS classroom "
         + "WHERE classroom.number = :classroomNumber "
         + "GROUP BY subject")
    List<Subject> findSubjectThatAreInClassroom(@Param("classroomNumber") String classroomNumber);

    @Deprecated
    List<Subject> findSubjectByName(@Param("name") String name);

    @Deprecated
    @Query("SELECT subject "
            + "FROM subject subject "
            + "JOIN subject.commissions AS commisions "
            + "JOIN commisions.schedules AS schedule "
            + "WHERE (schedule.startTime BETWEEN :startTime AND :endTime) "
            +   "OR (schedule.endTime BETWEEN :startTime AND :endTime) "
            + "GROUP BY subject")
    List<Subject> findSubjectsBetweenHours(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Deprecated
    @Query("SELECT subject "
        + "FROM subject subject "
        + "JOIN subject.commissions AS commissions "
        + "JOIN commissions.schedules AS schedule "
        + "WHERE schedule.day=:aDay "
        + "GROUP BY subject"
     )
    List<Subject> getAllSubjectsDictatedInTheDay(@Param("aDay") Day aDay);

    @Query("SELECT subject.name "
         + "FROM subject subject "
         + "GROUP BY subject.name")
    List<String> getAllSubjectsNames();

    Page<Subject> findAllByOrderByNameAsc(Pageable pageable);

    List<Subject> findAllBySubjectCodeInOrderBySubjectCodeAsc(List<String> subjectCodes);

    @Query("SELECT subject "
            + "FROM subject subject "
            + "JOIN subject.commissions AS commisions "
            + "JOIN commisions.schedules AS schedule "
            + "WHERE schedule.notices.size > 0 "
            + "GROUP BY subject")
    Page<Subject> findOverlappingSubjects(Pageable pageable);

    @Query("SELECT subject.subjectCode FROM subject subject")
    List<String> getAllSubjectCodes();
}

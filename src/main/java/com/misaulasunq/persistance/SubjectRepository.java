package com.misaulasunq.persistance;

import com.misaulasunq.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query("SELECT subject "
         + "FROM subject subject "
         + "LEFT JOIN subject.commissions AS commisions "
         + "LEFT JOIN commisions.schedules AS schedules "
         + "LEFT JOIN schedules.classroom AS classroom "
         + "WHERE classroom.number = :classroomNumber"
         )
    List<Subject> findSubjectThatAreInClassroom(@Param("classroomNumber") String classroomNumber);
}

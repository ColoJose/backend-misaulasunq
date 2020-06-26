package com.misaulasunq.persistance;

import com.misaulasunq.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Integer> {

    Optional<Classroom> findClassroomsByNumberEquals(String number);

    @Query("SELECT classroom.number "
            + "FROM Classroom classroom "
            + "GROUP BY classroom.number")
    List<String> getAllClassroomsNumbers();

    @Query("SELECT classroom.number "
         + "FROM Classroom classroom "
         + "WHERE classroom.number = :id")
    Classroom findByNumber(String id);

    @Query("SELECT classroom "
        + "FROM Classroom classroom "
        + "WHERE classroom.number = :id")
    Classroom findClassroomByNumber(String id);

    @Query("SELECT classroom "
        +  "FROM Classroom classroom "
        +  "WHERE classroom.number in :classroomNumbers"
    )
    List<Classroom> getClassroomByNumbers(List<String> classroomNumbers);

    List<Classroom> findAllByNumberInOrderByNumberAsc(Set<String> classroomNumbers);
}

package com.misaulasunq.persistance;

import com.misaulasunq.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Integer> {

    Optional<Classroom> findClassroomsByNumberEquals(String number);

    @Query("SELECT classroom.number "
            + "FROM Classroom classroom "
            + "GROUP BY classroom.number")
    List<String> getAllClassroomsNumbers();

    List<Classroom> findAllByNumberIn(List<String> classroomNumbers);
}

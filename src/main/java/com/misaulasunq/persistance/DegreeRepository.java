package com.misaulasunq.persistance;

import com.misaulasunq.model.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DegreeRepository extends JpaRepository<Degree,Integer> {

    List<Degree> findAllByCodeInOrderByCodeAsc(List<String> codes);
}

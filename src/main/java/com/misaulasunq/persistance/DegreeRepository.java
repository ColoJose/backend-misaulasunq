package com.misaulasunq.persistance;

import com.misaulasunq.model.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends JpaRepository<Degree,Integer> {

}

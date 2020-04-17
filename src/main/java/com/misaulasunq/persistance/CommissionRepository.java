package com.misaulasunq.persistance;

import com.misaulasunq.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionRepository extends JpaRepository<Commission,Integer> {
}

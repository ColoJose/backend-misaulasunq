package com.misaulasunq.persistance;

import com.misaulasunq.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionRepository extends JpaRepository<Commission,Integer> {

    @Modifying
    @Query("delete from Commission com where com.subject.id = 11")
    void deleteAllById(Integer id);
}

package com.misaulasunq.persistance;

import com.misaulasunq.model.OverlapNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OverlapNoticeRepository extends JpaRepository<OverlapNotice,Integer> {
}

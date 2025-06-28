package com.product.adapter.out.persistence.jpa.shard2;

import com.product.domain.model.RecommendType;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface RecommendShard2Repository extends JpaRepository<RecommendShard2Entity, Long> {

    @Query("SELECT r FROM RecommendShard2Entity r join fetch r.product WHERE r.checkDate = :checkDate AND r.type = :type")
    List<RecommendShard2Entity> findByCheckDateAndType(LocalDate checkDate, RecommendType type);
}

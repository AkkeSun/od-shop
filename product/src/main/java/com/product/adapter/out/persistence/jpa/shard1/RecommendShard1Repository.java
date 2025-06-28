package com.product.adapter.out.persistence.jpa.shard1;

import com.product.domain.model.RecommendType;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface RecommendShard1Repository extends JpaRepository<RecommendShard1Entity, Long> {

    @Query("SELECT r FROM RecommendShard1Entity r join fetch r.product WHERE r.checkDate = :checkDate AND r.type = :type")
    List<RecommendShard1Entity> findByCheckDateAndType(LocalDate checkDate, RecommendType type);
}

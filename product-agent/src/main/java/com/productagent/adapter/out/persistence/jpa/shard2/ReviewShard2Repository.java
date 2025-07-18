package com.productagent.adapter.out.persistence.jpa.shard2;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface ReviewShard2Repository extends JpaRepository<ReviewShard2Entity, Long> {

    void deleteByCustomerId(Long customerId);

    void deleteAllByProductIdIn(List<Long> productIds);

    List<ReviewShard2Entity> findAllByRegDateTimeBetween(LocalDateTime start, LocalDateTime end);

}

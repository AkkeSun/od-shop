package com.productagent.adapter.out.persistence.jpa.shard1;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface ReviewShard1Repository extends JpaRepository<ReviewShard1Entity, Long> {

    void deleteByCustomerId(Long customerId);

    void deleteAllByProductIdIn(List<Long> productIds);
}

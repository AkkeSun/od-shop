package com.productagent.adapter.out.persistence.jpa.shard1;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductMetricShard1Repository extends JpaRepository<ProductMetricShard1Entity, Long> {

    List<ProductMetricShard1Entity> findByNeedsEsUpdate(boolean needsEsUpdate);
}

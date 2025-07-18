package com.productagent.adapter.out.persistence.jpa.shard2;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductMetricShard2Repository extends JpaRepository<ProductMetricShard2Entity, Long> {

    List<ProductMetricShard2Entity> findByNeedsEsUpdate(boolean needsEsUpdate);

}

package com.product.adapter.out.persistence.jpa.shard1;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductMetricShard1Repository extends JpaRepository<ProductMetricShard1Entity, Long> {

    void deleteById(Long id);
}

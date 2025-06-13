package com.product.adapter.out.persistence.jpa.shard1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ProductMetricShard1Repository extends JpaRepository<ProductMetricShard1Entity, Long> {

    @Modifying
    @Query("delete from ProductMetricShard1Entity p where p.id = :id")
    void deleteById(Long id);
}

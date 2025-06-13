package com.product.adapter.out.persistence.jpa.shard2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ProductMetricShard2Repository extends JpaRepository<ProductMetricShard2Entity, Long> {

    @Modifying
    @Query("delete from ProductMetricShard2Entity p where p.id = :id")
    void deleteById(Long id);
}

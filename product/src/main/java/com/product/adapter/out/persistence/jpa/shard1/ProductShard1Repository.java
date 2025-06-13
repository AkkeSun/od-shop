package com.product.adapter.out.persistence.jpa.shard1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ProductShard1Repository extends JpaRepository<ProductShard1Entity, Long> {

    @Modifying
    @Query("delete from ProductShard1Entity p where p.id = :id")
    void deleteById(Long id);
}

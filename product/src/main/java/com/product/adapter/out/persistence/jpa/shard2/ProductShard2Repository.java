package com.product.adapter.out.persistence.jpa.shard2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ProductShard2Repository extends JpaRepository<ProductShard2Entity, Long> {

    @Modifying
    @Query("delete from ProductShard2Entity p where p.id = :id")
    void deleteById(Long id);
}

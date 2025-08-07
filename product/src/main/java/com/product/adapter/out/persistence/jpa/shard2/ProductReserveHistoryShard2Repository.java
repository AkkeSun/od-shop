package com.product.adapter.out.persistence.jpa.shard2;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductReserveHistoryShard2Repository extends
    JpaRepository<ProductReserveHistoryShard2Entity, Long> {

}

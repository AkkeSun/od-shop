package com.product.adapter.out.persistence.jpa.shard1;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductReserveHistoryShard1Repository extends
    JpaRepository<ProductReserveHistoryShard1Entity, Long> {

}

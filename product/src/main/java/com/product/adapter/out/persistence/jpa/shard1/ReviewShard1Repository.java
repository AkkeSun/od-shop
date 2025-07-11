package com.product.adapter.out.persistence.jpa.shard1;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface ReviewShard1Repository extends JpaRepository<ReviewShard1Entity, Long> {

    List<ReviewShard1Entity> findByProductId(Long productId, Pageable pageable);

    void deleteByProductId(Long productId);

    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);
}

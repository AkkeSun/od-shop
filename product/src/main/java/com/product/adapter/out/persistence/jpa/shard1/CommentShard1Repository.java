package com.product.adapter.out.persistence.jpa.shard1;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface CommentShard1Repository extends JpaRepository<CommentShard1Entity, Long> {

    List<CommentShard1Entity> findByProductId(Long productId, Pageable pageable);

    void deleteByProductId(Long productId);

    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);
}

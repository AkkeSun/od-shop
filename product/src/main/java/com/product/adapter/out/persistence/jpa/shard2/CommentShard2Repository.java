package com.product.adapter.out.persistence.jpa.shard2;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface CommentShard2Repository extends JpaRepository<CommentShard2Entity, Long> {

    List<CommentShard2Entity> findByProductId(Long productId, Pageable pageable);

    void deleteByProductId(Long productId);

    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);
}

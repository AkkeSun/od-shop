package com.productagent.adapter.out.persistence.jpa.shard2;

import com.productagent.application.port.out.ReviewStoragePort;
import com.productagent.domain.model.Review;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "primaryTransactionManager")
public class ReviewShard2Adapter implements ReviewStoragePort {

    private final ReviewShard2Repository repository;

    @Override
    public void deleteByProductIds(List<Long> productIds) {
        repository.deleteAllByProductIdIn(productIds);
    }

    @Override
    public void deleteByCustomerId(Long customerId) {
        repository.deleteByCustomerId(customerId);
    }

    @Override
    public List<Review> findByRegDateTime(LocalDateTime start, LocalDateTime end) {
        return repository.findAllByRegDateTimeBetween(start, end).stream()
            .map(ReviewShard2Entity::toDomain)
            .toList();
    }
}

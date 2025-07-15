package com.productagent.adapter.out.persistence.jpa.shard1;

import com.productagent.application.port.out.ReviewStoragePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "primaryTransactionManager")
public class ReviewShard1Adapter implements ReviewStoragePort {

    private final ReviewShard1Repository repository;

    @Override
    public void deleteByProductIds(List<Long> productIds) {
        repository.deleteAllByProductIdIn(productIds);
    }

    @Override
    public void deleteByCustomerId(Long customerId) {
        repository.deleteByCustomerId(customerId);
    }
}

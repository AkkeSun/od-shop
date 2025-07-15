package com.productagent.adapter.out.persistence.jpa;

import com.productagent.adapter.out.persistence.jpa.shard1.ReviewShard1Adapter;
import com.productagent.adapter.out.persistence.jpa.shard2.ReviewShard2Adapter;
import com.productagent.application.port.out.ReviewStoragePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
class ReviewStorageAdapter implements ReviewStoragePort {

    private final ReviewShard1Adapter shard1Adapter;
    private final ReviewShard2Adapter shard2Adapter;

    @Override
    public void deleteByProductIds(List<Long> productIds) {
        shard1Adapter.deleteByProductIds(productIds);
        shard2Adapter.deleteByProductIds(productIds);
    }

    @Override
    public void deleteByCustomerId(Long customerId) {
        shard1Adapter.deleteByCustomerId(customerId);
        shard2Adapter.deleteByCustomerId(customerId);
    }
}

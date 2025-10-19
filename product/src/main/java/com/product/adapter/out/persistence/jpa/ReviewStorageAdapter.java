package com.product.adapter.out.persistence.jpa;

import com.product.adapter.out.persistence.jpa.shard1.ReviewShard1Adapter;
import com.product.adapter.out.persistence.jpa.shard2.ReviewShard2Adapter;
import com.product.application.port.in.command.FindReviewListCommand;
import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.application.port.out.ReviewStoragePort;
import com.product.domain.model.Review;
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
    public List<Review> findByProductId(FindReviewListCommand command) {
        return ShardKeyUtil.isShard1(command.productId()) ?
            shard1Adapter.findByProductId(command) : shard2Adapter.findByProductId(command);
    }

    @Override
    public void register(Review comment) {
        if (ShardKeyUtil.isShard1(comment.productId())) {
            shard1Adapter.register(comment);
        } else {
            shard2Adapter.register(comment);
        }
    }

    @Override
    public boolean existsByCustomerIdAndProductId(RegisterReviewCommand command) {
        return ShardKeyUtil.isShard1(command.productId()) ?
            shard1Adapter.existsByCustomerIdAndProductId(command)
            : shard2Adapter.existsByCustomerIdAndProductId(command);
    }

    @Override
    public void deleteByProductId(Long productId) {
        if (ShardKeyUtil.isShard1(productId)) {
            shard1Adapter.deleteByProductId(productId);
        } else {
            shard2Adapter.deleteByProductId(productId);
        }
    }
}

package com.product.adapter.out.persistence.jpa.shard2;

import com.product.application.port.in.command.FindReviewListCommand;
import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.application.port.out.ReviewStoragePort;
import com.product.domain.model.Review;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "secondaryTransactionManager")
public class ReviewShard2Adapter implements ReviewStoragePort {

    private final ReviewShard2Repository repository;

    @Override
    public List<Review> findByProductId(FindReviewListCommand command) {
        List<ReviewShard2Entity> entities = repository.findByProductId(command.productId(),
            command.pageable());

        return entities.stream()
            .map(ReviewShard2Entity::toDomain)
            .toList();
    }

    @Override
    public void register(Review comment) {
        repository.save(ReviewShard2Entity.of(comment));
    }

    @Override
    public boolean existsByCustomerIdAndProductId(RegisterReviewCommand command) {
        return repository.existsByCustomerIdAndProductId(command.account().id(),
            command.productId());
    }

    @Override
    public void deleteByProductId(Long productId) {
        repository.deleteByProductId(productId);
    }
}

package com.productagent.fakeClass;

import com.productagent.application.port.out.ReviewStoragePort;
import com.productagent.domain.model.Review;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeReviewStoragePort implements ReviewStoragePort {

    public List<Review> database = new ArrayList<>();
    public boolean shouldThrowException = false;

    @Override
    public void deleteByProductIds(List<Long> productIds) {
        database.removeIf(review -> productIds.contains(review.productId()));
        log.info("FakeReviewStoragePort deleted reviews by productIds={}", productIds);
    }

    @Override
    public void deleteByCustomerId(Long customerId) {
        database.removeIf(review -> review.customerId().equals(customerId));
        log.info("FakeReviewStoragePort deleted reviews by customerId={}", customerId);
    }

    @Override
    public List<Review> findByRegDateTime(LocalDateTime start, LocalDateTime end) {
        return database.stream()
            .filter(review -> {
                LocalDateTime regDateTime = review.regDateTime();
                return (regDateTime.isEqual(start) || regDateTime.isAfter(start))
                    && (regDateTime.isBefore(end) || regDateTime.isEqual(end));
            })
            .toList();
    }
}

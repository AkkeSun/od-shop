package com.productagent.application.port.out;

import com.productagent.domain.model.Review;
import java.time.LocalDateTime;
import java.util.List;

public interface ReviewStoragePort {

    void deleteByProductIds(List<Long> productIds);

    void deleteByCustomerId(Long customerId);

    List<Review> findByRegDateTime(LocalDateTime start, LocalDateTime end);
}

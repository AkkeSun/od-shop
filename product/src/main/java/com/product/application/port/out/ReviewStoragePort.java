package com.product.application.port.out;

import com.product.application.port.in.command.FindReviewListCommand;
import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.domain.model.Review;
import java.util.List;

public interface ReviewStoragePort {

    List<Review> findByProductId(FindReviewListCommand command);

    void register(Review comment);

    boolean existsByCustomerIdAndProductId(RegisterReviewCommand command);

    void deleteByProductId(Long productId);
}

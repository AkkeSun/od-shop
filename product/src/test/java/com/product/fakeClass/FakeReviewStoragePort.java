package com.product.fakeClass;

import com.product.application.port.in.command.FindReviewListCommand;
import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.application.port.out.ReviewStoragePort;
import com.product.domain.model.Review;
import java.util.ArrayList;
import java.util.List;

public class FakeReviewStoragePort implements ReviewStoragePort {

    public List<Review> database = new ArrayList<>();

    @Override
    public List<Review> findByProductId(FindReviewListCommand command) {
        return database.stream().filter(comment -> command.productId().equals(command.productId()))
            .limit(command.pageable().getPageSize())
            .toList();
    }

    @Override
    public void register(Review comment) {
        database.add(comment);
    }

    @Override
    public boolean existsByCustomerIdAndProductId(RegisterReviewCommand command) {
        return !database.stream()
            .filter(data -> data.customerId().equals(command.account().id()))
            .filter(data -> data.productId().equals(command.productId()))
            .toList().isEmpty();
    }

    @Override
    public void deleteByProductId(Long productId) {
        database.removeIf(comment -> comment.productId().equals(productId));
    }
}

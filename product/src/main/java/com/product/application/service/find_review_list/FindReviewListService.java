package com.product.application.service.find_review_list;

import com.product.application.port.in.FindReviewListUseCase;
import com.product.application.port.in.command.FindReviewListCommand;
import com.product.application.port.out.ReviewStoragePort;
import com.product.domain.model.Review;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindReviewListService implements FindReviewListUseCase {

    private final ReviewStoragePort commentStoragePort;

    @NewSpan
    @Override
    public FindReviewListServiceResponse findReviewList(FindReviewListCommand command) {
        List<Review> comments = commentStoragePort.findByProductId(command);
        return FindReviewListServiceResponse.of(comments, command);
    }
}

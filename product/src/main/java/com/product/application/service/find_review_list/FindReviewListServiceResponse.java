package com.product.application.service.find_review_list;

import com.product.application.port.in.command.FindReviewListCommand;
import com.product.domain.model.Review;
import java.util.List;
import lombok.Builder;

@Builder
public record FindReviewListServiceResponse(
    Long productId,
    int page,
    int size,
    int reviewCount,
    List<FindReviewListServiceResponseItem> reviews

) {

    static FindReviewListServiceResponse of(List<Review> reviews,
        FindReviewListCommand command) {
        return FindReviewListServiceResponse.builder()
            .productId(command.productId())
            .page(command.pageable().getPageNumber())
            .size(command.pageable().getPageSize())
            .reviewCount(reviews.size())
            .reviews(reviews.stream()
                .map(FindReviewListServiceResponseItem::of)
                .toList())
            .build();
    }

    @Builder
    public record FindReviewListServiceResponseItem(
        String customerEmail,
        String review
    ) {

        static FindReviewListServiceResponseItem of(Review review) {
            return FindReviewListServiceResponseItem.builder()
                .customerEmail(review.customerEmail())
                .review(review.review())
                .build();
        }
    }
}

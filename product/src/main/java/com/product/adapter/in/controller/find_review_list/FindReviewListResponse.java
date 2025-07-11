package com.product.adapter.in.controller.find_review_list;

import com.product.application.service.find_review_list.FindReviewListServiceResponse;
import com.product.application.service.find_review_list.FindReviewListServiceResponse.FindReviewListServiceResponseItem;
import java.util.List;
import lombok.Builder;

@Builder
record FindReviewListResponse(
    Long productId,
    int page,
    int size,
    int reviewCount,
    List<FindReviewListResponseItem> reviews
) {

    static FindReviewListResponse of(FindReviewListServiceResponse serviceResponse) {
        return FindReviewListResponse.builder()
            .productId(serviceResponse.productId())
            .page(serviceResponse.page())
            .size(serviceResponse.size())
            .reviewCount(serviceResponse.reviewCount())
            .reviews(serviceResponse.reviews().stream()
                .map(FindReviewListResponseItem::of)
                .toList())
            .build();
    }

    @Builder
    record FindReviewListResponseItem(
        String customerEmail,
        String review
    ) {

        static FindReviewListResponseItem of(FindReviewListServiceResponseItem item) {
            return FindReviewListResponseItem.builder()
                .customerEmail(item.customerEmail())
                .review(item.review())
                .build();
        }
    }
}

package com.product.adapter.in.controller.find_review_list;

import com.product.application.service.find_review_list.FindReviewListServiceResponse;
import com.product.application.service.find_review_list.FindReviewListServiceResponse.FindReviewListServiceResponseItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindReviewListResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답을 API 응답으로 변환한다.")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답을 API 응답으로 변환한다.")
        void success() {
            // given
            FindReviewListServiceResponse serviceResponse = FindReviewListServiceResponse.builder()
                .page(0)
                .size(10)
                .reviewCount(1)
                .reviews(List.of(FindReviewListServiceResponseItem.builder()
                    .review("좋아요")
                    .customerEmail("test")
                    .build()))
                .build();

            // when
            FindReviewListResponse response = FindReviewListResponse.of(serviceResponse);

            // then
            assert response.page() == serviceResponse.page();
            assert response.size() == serviceResponse.size();
            assert response.reviewCount() == serviceResponse.reviewCount();
            assert response.reviews().size() == serviceResponse.reviews().size();
            assert response.reviews().get(0).review().equals("좋아요");
            assert response.reviews().get(0).customerEmail().equals("test");

        }
    }
}
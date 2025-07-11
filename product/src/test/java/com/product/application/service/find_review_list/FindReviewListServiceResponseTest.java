package com.product.application.service.find_review_list;

import com.product.application.port.in.command.FindReviewListCommand;
import com.product.domain.model.Review;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

class FindReviewListServiceResponseTest {

    @Nested
    @DisplayName("[of] command 와 도메인 리스트로 서비스 응답 객체를 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] command 와 도메인 리스트로 서비스 응답 객체로 변환하는지 확인한다.")
        void success() {
            // given
            List<Review> reviews = List.of(Review.builder()
                .customerEmail("email")
                .review("comment")
                .build());
            FindReviewListCommand command = FindReviewListCommand.builder()
                .productId(1L)
                .pageable(PageRequest.of(0, 10))
                .build();

            // when
            FindReviewListServiceResponse response = FindReviewListServiceResponse.of(reviews,
                command);

            // then
            assert response.productId().equals(command.productId());
            assert response.page() == command.pageable().getPageNumber();
            assert response.size() == command.pageable().getPageSize();
            assert response.reviewCount() == reviews.size();
            assert response.reviews().size() == reviews.size();
            assert response.reviews().get(0).customerEmail().equals("email");
            assert response.reviews().get(0).review().equals("comment");
        }
    }
}
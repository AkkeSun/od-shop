package com.product.application.service.find_review_list;

import com.product.application.port.in.command.FindReviewListCommand;
import com.product.application.port.out.ReviewStoragePort;
import com.product.domain.model.Review;
import com.product.fakeClass.FakeReviewStoragePort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

class FindReviewListServiceTest {

    private final ReviewStoragePort reviewStoragePort;
    private final FindReviewListService findReviewListService;

    FindReviewListServiceTest() {
        this.reviewStoragePort = new FakeReviewStoragePort();
        this.findReviewListService = new FindReviewListService(reviewStoragePort);
    }

    @Nested
    @DisplayName("[findCommentList] 댓글 목록 조회 메소드")
    class Describe_findCommentList {

        @Test
        @DisplayName("[success] 댓글 목록을 성공적으로 조회하는지 확인한다.")
        void success() {
            // given
            Review review = Review.builder()
                .id(1L)
                .productId(1L)
                .review("This is a test comment")
                .customerEmail("email")
                .regDate(LocalDate.of(2025, 1, 1))
                .regDateTime(LocalDateTime.of(2025, 1, 1, 12, 0, 0))
                .build();
            reviewStoragePort.register(review);
            FindReviewListCommand command = FindReviewListCommand.builder()
                .productId(1L)
                .pageable(PageRequest.of(0, 10))
                .build();

            // when
            FindReviewListServiceResponse response = findReviewListService.findReviewList(
                command);

            // then
            assert response.productId().equals(review.productId());
            assert response.page() == 0;
            assert response.size() == 10;
            assert response.reviews().size() == 1;
            assert response.reviews().get(0).review().equals(review.review());
            assert response.reviews().get(0).customerEmail().equals(review.customerEmail());

        }
    }
}
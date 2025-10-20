package com.product.adapter.out.persistence.jpa.shard1;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.product.IntegrationTestSupport;
import com.product.application.port.in.command.FindReviewListCommand;
import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.domain.model.Review;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class ReviewShard1AdapterTest extends IntegrationTestSupport {

    @Autowired
    ReviewShard1Adapter adapter;
    @Autowired
    ReviewShard1Repository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Nested
    @DisplayName("[findByProductId] 상품 ID로 리뷰를 조회하는 API")
    class Describe_findByProductId {

        @Test
        @DisplayName("[error] 리뷰가 존재하지 않을 때 빈 리스트를 응답한다.")
        void error() {
            // given
            FindReviewListCommand command = FindReviewListCommand.builder()
                .productId(2L)
                .pageable(PageRequest.of(0, 10))
                .build();

            // when
            List<Review> result = adapter.findByProductId(command);

            // then
            assert result.isEmpty();
        }

        @Test
        @DisplayName("[success] 리뷰가 존재할 때 리뷰를 응답한다.")
        void success() {
            // given
            FindReviewListCommand command = FindReviewListCommand.builder()
                .productId(1L)
                .pageable(PageRequest.of(0, 10))
                .build();
            ReviewShard1Entity entity1 = repository.save(ReviewShard1Entity.builder()
                .id(123L)
                .productId(command.productId())
                .review("test comment")
                .customerId(10L)
                .score(3.5)
                .regDate(LocalDate.of(2025, 1, 1))
                .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build());
            ReviewShard1Entity entity2 = repository.save(ReviewShard1Entity.builder()
                .id(124L)
                .productId(command.productId())
                .review("another comment")
                .customerId(20L)
                .score(4.0)
                .regDate(LocalDate.of(2025, 1, 2))
                .regDateTime(LocalDateTime.of(2025, 1, 2, 0, 0, 0))
                .build());

            // when
            List<Review> result = adapter.findByProductId(command);

            // then
            assert result.size() == 2;
            assert result.get(0).id().equals(entity1.getId());
            assert result.get(1).id().equals(entity2.getId());
            assert result.get(0).review().equals(entity1.getReview());
            assert result.get(1).review().equals(entity2.getReview());
            assert result.get(0).productId().equals(entity1.getProductId());
            assert result.get(1).productId().equals(entity2.getProductId());
            assert result.get(0).customerId().equals(entity1.getCustomerId());
            assert result.get(1).customerId().equals(entity2.getCustomerId());
            assert result.get(0).score() == entity1.getScore();
            assert result.get(1).score() == entity2.getScore();
            assert result.get(0).regDate().equals(entity1.getRegDate());
            assert result.get(1).regDate().equals(entity2.getRegDate());
            assert result.get(0).regDateTime().equals(entity1.getRegDateTime());
            assert result.get(1).regDateTime().equals(entity2.getRegDateTime());
            assert result.get(0).id().equals(entity1.getId());
            assert result.get(1).id().equals(entity2.getId());
            assert result.get(0).regDate().equals(entity1.getRegDate());
            assert result.get(1).regDate().equals(entity2.getRegDate());
            assert result.get(0).regDateTime().equals(entity1.getRegDateTime());
            assert result.get(1).regDateTime().equals(entity2.getRegDateTime());
        }
    }

    @Nested
    @DisplayName("[register] 리뷰를 등록하는 API")
    class Describe_register {

        @Test
        @DisplayName("[success] 리뷰를 등록한다.")
        void success() {
            // given
            Review comment = Review.builder()
                .id(123L)
                .productId(1L)
                .customerId(10L)
                .review("Great product!")
                .score(4.5)
                .regDate(LocalDate.of(2025, 1, 1))
                .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();

            // when
            adapter.register(comment);
            ReviewShard1Entity entity = repository.findById(comment.id()).get();

            // then
            assert entity.getId().equals(comment.id());
            assert entity.getProductId().equals(comment.productId());
            assert entity.getCustomerId().equals(comment.customerId());
            assert entity.getReview().equals(comment.review());
            assert entity.getScore() == comment.score();
            assert entity.getRegDate().equals(comment.regDate());
            assert entity.getRegDateTime().equals(comment.regDateTime());
        }
    }

    @Nested
    @DisplayName("[deleteByProductId] 상품 ID로 리뷰를 삭제하는 API")
    class Describe_deleteByproductId {

        @Test
        @DisplayName("[success] 상품 ID로 리뷰를 삭제한다.")
        void success() {
            // given
            Review comment = Review.builder()
                .id(123L)
                .productId(1L)
                .customerId(10L)
                .review("Great product!")
                .score(4.5)
                .regDate(LocalDate.of(2025, 1, 1))
                .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();
            adapter.register(comment);
            assert repository.findById(comment.id()).isPresent();

            // when
            adapter.deleteByProductId(comment.productId());

            // then
            assert !repository.findById(comment.id()).isPresent();
        }


        @Nested
        @DisplayName("[existsByCustomerIdAndProductId] 고객 아이디와 상품아이디로 리뷰 등록 유무를 조회하는 메소드")
        class Describe_existsByCustomerIdAndProductId {

            @Test
            @DisplayName("[success] 조회된 리뷰가 있으면 true 를 응답한다.")
            void success() {
                // given
                Review review = Review.builder()
                    .id(123L)
                    .productId(1L)
                    .customerId(10L)
                    .review("Great product!")
                    .score(4.5)
                    .regDate(LocalDate.of(2025, 1, 1))
                    .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                    .build();
                adapter.register(review);
                assert repository.findById(review.id()).isPresent();

                // when
                boolean result = adapter.existsByCustomerIdAndProductId(
                    RegisterReviewCommand.builder()
                        .productId(review.productId())
                        .loginInfo(LoginAccountInfo.builder().id(review.customerId()).build())
                        .build()
                );

                // then
                assert result;
            }

            @Test
            @DisplayName("[success] 조회된 리뷰가 없으면 false 를 응답한다.")
            void success2() {
                // given
                Review review = Review.builder()
                    .id(123L)
                    .productId(1L)
                    .customerId(10L)
                    .review("Great product!")
                    .score(4.5)
                    .regDate(LocalDate.of(2025, 1, 1))
                    .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                    .build();
                // when
                boolean result = adapter.existsByCustomerIdAndProductId(
                    RegisterReviewCommand.builder()
                        .productId(review.productId())
                        .loginInfo(LoginAccountInfo.builder().id(review.customerId()).build())
                        .build()
                );

                // then
                assert !result;
            }
        }
    }
}
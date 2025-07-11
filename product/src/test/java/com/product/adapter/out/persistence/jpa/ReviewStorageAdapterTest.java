package com.product.adapter.out.persistence.jpa;

import com.product.IntegrationTestSupport;
import com.product.adapter.out.persistence.jpa.shard1.ReviewShard1Adapter;
import com.product.adapter.out.persistence.jpa.shard2.ReviewShard2Adapter;
import com.product.application.port.in.command.FindReviewListCommand;
import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.domain.model.Account;
import com.product.domain.model.Review;
import com.product.infrastructure.util.ShardKeyUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class ReviewStorageAdapterTest extends IntegrationTestSupport {

    @Autowired
    ReviewStorageAdapter adapter;
    @Autowired
    ReviewShard1Adapter shard1Adapter;
    @Autowired
    ReviewShard2Adapter shard2Adapter;


    @Nested
    @DisplayName("[register] 리뷰를 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 상품 아이디가 shard1에 해당하면 shard1에 상품 정보를 저장한다.")
        void success1() {
            // given
            Review review = getComment(true);

            // when
            adapter.register(review);
            Review result = shard1Adapter.findByProductId(
                    FindReviewListCommand.builder()
                        .productId(review.productId())
                        .pageable(PageRequest.of(0, 10))
                        .build())
                .getFirst();

            // then
            assert result.productId().equals(review.productId());
            assert result.review().equals(review.review());
            assert result.customerEmail().equals(review.customerEmail());
            assert result.regDate().equals(review.regDate());
            assert result.regDateTime().equals(review.regDateTime());
        }

        @Test
        @DisplayName("[success] 상품 아이디가 shard2에 해당하면 shard2에 상품 정보를 저장한다.")
        void success2() {
            // given
            Review review = getComment(false);

            // when
            adapter.register(review);
            Review result = shard2Adapter.findByProductId(
                    FindReviewListCommand.builder()
                        .productId(review.productId())
                        .pageable(PageRequest.of(0, 10))
                        .build())
                .getFirst();

            // then
            assert result.productId().equals(review.productId());
            assert result.review().equals(review.review());
            assert result.customerEmail().equals(review.customerEmail());
            assert result.regDate().equals(review.regDate());
            assert result.regDateTime().equals(review.regDateTime());
        }
    }

    @Nested
    @DisplayName("[findByProductId] 상품 아이디로 리뷰를 조회하는 메소드")
    class Describe_findByProductId {

        @Test
        @DisplayName("[success] 상품 아이디가 shard1에 해당하면 shard1에서 상품 정보를 조회한다.")
        void success1() {
            // given
            Review review = getComment(true);
            shard1Adapter.register(review);

            // when
            Review result = adapter.findByProductId(
                FindReviewListCommand.builder()
                    .productId(review.productId())
                    .pageable(PageRequest.of(0, 10))
                    .build()).getFirst();

            // then
            assert result.id().equals(review.id());
            assert result.productId().equals(review.productId());
            assert result.review().equals(review.review());
            assert result.customerEmail().equals(review.customerEmail());
            assert result.regDate().equals(review.regDate());
            assert result.regDateTime().equals(review.regDateTime());
        }

        @Test
        @DisplayName("[success] 상품 아이디가 shard2에 해당하면 shard2에서 상품 정보를 조회한다.")
        void success2() {
            // given
            Review review = getComment(false);
            shard2Adapter.register(review);

            // when
            Review result = adapter.findByProductId(
                FindReviewListCommand.builder()
                    .productId(review.productId())
                    .pageable(PageRequest.of(0, 10))
                    .build()).getFirst();

            // then
            assert result.id().equals(review.id());
            assert result.productId().equals(review.productId());
            assert result.review().equals(review.review());
            assert result.customerEmail().equals(review.customerEmail());
            assert result.regDate().equals(review.regDate());
            assert result.regDateTime().equals(review.regDateTime());
        }
    }

    @Nested
    @DisplayName("[deleteByProductId] 상품 ID로 리뷰를 삭제하는 API")
    class Describe_deleteByproductId {

        @Test
        @DisplayName("[success] 상품 아이디가 shard1에 해당하면 shard1에서 상품 정보를 삭제한다.")
        void success() {
            // given
            Review comment = getComment(true);
            shard1Adapter.register(comment);
            assert shard1Adapter.findByProductId(
                FindReviewListCommand.builder()
                    .productId(comment.productId())
                    .pageable(PageRequest.of(0, 10))
                    .build()).size() == 1;

            // when
            adapter.deleteByProductId(comment.productId());
            List<Review> result = shard1Adapter.findByProductId(
                FindReviewListCommand.builder()
                    .productId(comment.productId())
                    .pageable(PageRequest.of(0, 10))
                    .build());

            // then
            assert result.isEmpty();
        }

        @Test
        @DisplayName("[success] 상품 아이디가 shard2에 해당하면 shard2에서 상품 정보를 삭제한다.")
        void success2() {
            // given
            Review comment = getComment(false);
            shard2Adapter.register(comment);
            assert shard2Adapter.findByProductId(FindReviewListCommand.builder()
                .productId(comment.productId())
                .pageable(PageRequest.of(0, 10))
                .build()).size() == 1;

            // when
            adapter.deleteByProductId(comment.productId());
            List<Review> result = shard1Adapter.findByProductId(
                FindReviewListCommand.builder()
                    .productId(comment.productId())
                    .pageable(PageRequest.of(0, 10))
                    .build());

            // then
            assert result.isEmpty();
        }
    }

    @Nested
    @DisplayName("[existsByCustomerIdAndProductId] 고객 아이디와 상품아이디로 리뷰 등록 유무를 조회하는 메소드")
    class Describe_existsByCustomerIdAndProductId {

        @Test
        @DisplayName("[success] 상품 아이디가 shard1에 해당하면 shard1에 상품 정보를 조회한다.")
        void success1() {
            // given
            Review comment = getComment(true);
            adapter.register(comment);

            // when
            boolean result = shard1Adapter.existsByCustomerIdAndProductId(
                RegisterReviewCommand.builder()
                    .productId(comment.productId())
                    .account(Account.builder().id(comment.customerId()).build())
                    .build());

            // then
            assert result;
        }

        @Test
        @DisplayName("[success] 상품 아이디가 shard2에 해당하면 shard1에 상품 정보를 조회한다.")
        void success2() {
            // given
            Review comment = getComment(false);
            adapter.register(comment);

            // when
            boolean result = shard2Adapter.existsByCustomerIdAndProductId(
                RegisterReviewCommand.builder()
                    .productId(comment.productId())
                    .account(Account.builder().id(comment.customerId()).build())
                    .build());

            // then
            assert result;
        }
    }

    private Review getComment(boolean isShard1) {
        long productId;
        if (isShard1) {
            while (true) {
                productId = snowflakeGenerator.nextId();
                if (ShardKeyUtil.isShard1(productId)) {
                    break;
                }
            }
        } else {
            while (true) {
                productId = snowflakeGenerator.nextId();
                if (!ShardKeyUtil.isShard1(productId)) {
                    break;
                }
            }
        }

        return Review.builder()
            .id(snowflakeGenerator.nextId())
            .productId(productId)
            .review("리뷰 내용")
            .customerEmail("작성자")
            .regDate(LocalDate.of(2025, 1, 1))
            .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
            .build();
    }
}
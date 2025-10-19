package com.product.application.service.register_review;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.domain.model.Product;
import com.product.domain.model.Review;
import com.product.fakeClass.DummyOrderClientPort;
import com.product.fakeClass.DummySnowflakeGenerator;
import com.product.fakeClass.FakeProductStoragePort;
import com.product.fakeClass.FakeReviewStoragePort;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.CustomBusinessException;
import com.product.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterReviewServiceTest {

    private final DummySnowflakeGenerator dummySnowflakeGenerator;
    private final DummyOrderClientPort dummyOrderClientPort;
    private final RegisterReviewService registerReviewService;
    private final FakeReviewStoragePort fakeReviewStoragePort;
    private final FakeProductStoragePort fakeProductStoragePort;

    RegisterReviewServiceTest() {
        this.dummySnowflakeGenerator = new DummySnowflakeGenerator();
        this.dummyOrderClientPort = new DummyOrderClientPort();
        this.fakeReviewStoragePort = new FakeReviewStoragePort();
        this.fakeProductStoragePort = new FakeProductStoragePort();
        this.registerReviewService = new RegisterReviewService(dummySnowflakeGenerator,
            dummyOrderClientPort, fakeProductStoragePort, fakeReviewStoragePort
        );
    }

    @AfterEach
    void tearDown() {
        fakeReviewStoragePort.database.clear();
        fakeProductStoragePort.database.clear();
    }

    @Nested
    @DisplayName("[registerComment] 리뷰를 등록하는 메소드")
    class Describe_registerComment {

        @Test
        @DisplayName("[success] 리뷰를 성공적으로 등록하는지 확인한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(10L)
                .deleteYn("N")
                .build();
            fakeProductStoragePort.database.add(product);
            RegisterReviewCommand command = RegisterReviewCommand.builder()
                .productId(product.getId())
                .account(Account.builder().id(1L)
                    .email("od")
                    .build())
                .review("comment")
                .productId(10L)
                .score(5.0)
                .build();

            // when
            RegisterReviewServiceResponse result = registerReviewService.registerReview(command);

            // then
            assert result.result();
        }

        @Test
        @DisplayName("[error] 상품을 구매한 사용자가 아니라면 예외를 응답한다.")
        void error() {
            // given
            Product product = Product.builder()
                .id(10L)
                .deleteYn("N")
                .build();
            fakeProductStoragePort.database.add(product);
            RegisterReviewCommand command = RegisterReviewCommand.builder()
                .productId(product.getId())
                .account(Account.builder().id(2L)
                    .email("od")
                    .build())
                .review("comment")
                .productId(10L)
                .score(5.0)
                .build();

            // when
            CustomAuthorizationException result = assertThrows(CustomAuthorizationException.class,
                () -> registerReviewService.registerReview(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.ACCESS_DENIED);
        }

        @Test
        @DisplayName("[error] 이미 리뷰를 등록했다면 예외를 응답한다.")
        void error2() {
            // given
            Product product = Product.builder()
                .id(10L)
                .deleteYn("N")
                .build();
            fakeProductStoragePort.database.add(product);
            RegisterReviewCommand command = RegisterReviewCommand.builder()
                .productId(product.getId())
                .account(Account.builder().id(1L)
                    .email("od")
                    .build())
                .review("comment")
                .productId(10L)
                .score(5.0)
                .build();
            fakeReviewStoragePort.database.add(Review.of(command, 10L));

            // when
            CustomBusinessException result = assertThrows(CustomBusinessException.class,
                () -> registerReviewService.registerReview(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.Business_SAVED_REVIEW_INFO);
        }
    }
}
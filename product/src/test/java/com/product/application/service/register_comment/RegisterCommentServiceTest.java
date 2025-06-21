package com.product.application.service.register_comment;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.application.port.in.command.RegisterCommentCommand;
import com.product.domain.model.Account;
import com.product.domain.model.Product;
import com.product.fakeClass.DummyOrderClientPort;
import com.product.fakeClass.DummySnowflakeGenerator;
import com.product.fakeClass.FakeCommentStoragePort;
import com.product.fakeClass.FakeProductStoragePort;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterCommentServiceTest {

    private final DummySnowflakeGenerator dummySnowflakeGenerator;
    private final DummyOrderClientPort dummyOrderClientPort;
    private final RegisterCommentService registerCommentService;
    private final FakeCommentStoragePort fakeCommentStoragePort;
    private final FakeProductStoragePort fakeProductStoragePort;

    RegisterCommentServiceTest() {
        this.dummySnowflakeGenerator = new DummySnowflakeGenerator();
        this.dummyOrderClientPort = new DummyOrderClientPort();
        this.fakeCommentStoragePort = new FakeCommentStoragePort();
        this.fakeProductStoragePort = new FakeProductStoragePort();
        this.registerCommentService = new RegisterCommentService(dummySnowflakeGenerator,
            dummyOrderClientPort, fakeProductStoragePort, fakeCommentStoragePort
        );
    }

    @AfterEach
    void tearDown() {
        fakeCommentStoragePort.database.clear();
        fakeProductStoragePort.database.clear();
    }

    @Nested
    @DisplayName("[registerComment] 댓글을 등록하는 메소드")
    class Describe_registerComment {

        @Test
        @DisplayName("[success] 댓글을 성공적으로 등록하는지 확인한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(10L)
                .build();
            fakeProductStoragePort.database.add(product);
            RegisterCommentCommand command = RegisterCommentCommand.builder()
                .productId(product.getId())
                .account(Account.builder().id(1L)
                    .email("od")
                    .build())
                .comment("comment")
                .productId(10L)
                .score(5.0)
                .build();

            // when
            RegisterCommentServiceResponse result = registerCommentService.registerComment(command);

            // then
            assert result.result();
        }

        @Test
        @DisplayName("[error] 상품을 구매한 사용자가 아니라면 예외를 응답한다.")
        void error() {
            // given
            Product product = Product.builder()
                .id(10L)
                .build();
            fakeProductStoragePort.database.add(product);
            RegisterCommentCommand command = RegisterCommentCommand.builder()
                .productId(product.getId())
                .account(Account.builder().id(2L)
                    .email("od")
                    .build())
                .comment("comment")
                .productId(10L)
                .score(5.0)
                .build();

            // when
            CustomAuthorizationException result = assertThrows(CustomAuthorizationException.class,
                () -> registerCommentService.registerComment(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.ACCESS_DENIED);
        }
    }
}
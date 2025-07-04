package com.product.application.service.delete_product;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Account;
import com.product.domain.model.Product;
import com.product.fakeClass.DummyMessageProducerPort;
import com.product.fakeClass.FakeCommentStoragePort;
import com.product.fakeClass.FakeElasticSearchClientPort;
import com.product.fakeClass.FakeProductStoragePort;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DeleteProductServiceTest {

    private final FakeProductStoragePort productStoragePort;
    private final CommentStoragePort commentStoragePort;
    private final FakeElasticSearchClientPort elasticSearchClientPort;
    private final DummyMessageProducerPort messageProducerPort;
    private final DeleteProductService deleteProductService;

    DeleteProductServiceTest() {
        this.productStoragePort = new FakeProductStoragePort();
        this.elasticSearchClientPort = new FakeElasticSearchClientPort();
        this.messageProducerPort = new DummyMessageProducerPort();
        this.commentStoragePort = new FakeCommentStoragePort();
        this.deleteProductService = new DeleteProductService(
            commentStoragePort,
            productStoragePort,
            messageProducerPort,
            elasticSearchClientPort
        );
        ReflectionTestUtils.setField(deleteProductService, "historyTopic", "test");
    }

    @AfterEach
    void tearDown() {
        productStoragePort.database.clear();
        elasticSearchClientPort.database.clear();
    }

    @Nested
    @DisplayName("[deleteProduct] 상품을 삭제하는 메소드")
    class Describe_deleteProduct {

        @Test
        @DisplayName("[error] 상품 셀러 아이디와 인증토큰 소유자 아이다가 다를 경우 예외를 응답한다.")
        void error() {
            // given
            Long ProductId = 1L;
            Account account = Account.builder()
                .email("test@gmail.com")
                .id(2L)
                .build();
            productStoragePort.register(
                Product.builder()
                    .id(ProductId)
                    .sellerId(3L)
                    .deleteYn("N")
                    .build()
            );

            // when
            CustomAuthorizationException result = assertThrows(CustomAuthorizationException.class,
                () -> deleteProductService.deleteProduct(ProductId, account));

            // then
            assert result.getErrorCode().equals(ErrorCode.ACCESS_DENIED);
        }

        @Test
        @DisplayName("[success] 상품이 잘 삭제되는지 확인한다.")
        void success() {
            // given
            Long productId = 1L;
            Account account = Account.builder()
                .email("test@gmail.com")
                .id(2L)
                .build();
            productStoragePort.register(
                Product.builder()
                    .id(productId)
                    .sellerId(account.id())
                    .deleteYn("N")
                    .build()
            );

            // when
            DeleteProductServiceResponse serviceResponse = deleteProductService
                .deleteProduct(productId, account);

            // then
            assert serviceResponse.result();
            assert productStoragePort.findByIdAndDeleteYn(productId, "N") == null;
            assert elasticSearchClientPort.database.stream()
                .noneMatch(product -> product.getId().equals(productId));
        }
    }
}
package com.product.application.service.delete_product;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.common.infrastructure.exception.CustomAuthorizationException;
import com.common.infrastructure.exception.ErrorCode;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.product.application.port.out.ReviewStoragePort;
import com.product.domain.model.Product;
import com.product.fakeClass.DummyMessageProducerPort;
import com.product.fakeClass.FakeElasticSearchClientPort;
import com.product.fakeClass.FakeProductStoragePort;
import com.product.fakeClass.FakeReviewStoragePort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DeleteProductServiceTest {

    private final FakeProductStoragePort productStoragePort;
    private final ReviewStoragePort reviewStoragePort;
    private final FakeElasticSearchClientPort elasticSearchClientPort;
    private final DummyMessageProducerPort messageProducerPort;
    private final DeleteProductService deleteProductService;

    DeleteProductServiceTest() {
        this.productStoragePort = new FakeProductStoragePort();
        this.elasticSearchClientPort = new FakeElasticSearchClientPort();
        this.messageProducerPort = new DummyMessageProducerPort();
        this.reviewStoragePort = new FakeReviewStoragePort();
        this.deleteProductService = new DeleteProductService(
            reviewStoragePort,
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
            LoginAccountInfo loginInfo = LoginAccountInfo.builder()
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
                () -> deleteProductService.deleteProduct(ProductId, loginInfo));

            // then
            assert result.getErrorCode().equals(ErrorCode.ACCESS_DENIED);
        }

        @Test
        @DisplayName("[success] 상품이 잘 삭제되는지 확인한다.")
        void success() {
            // given
            Long productId = 1L;
            LoginAccountInfo loginInfo = LoginAccountInfo.builder()
                .email("test@gmail.com")
                .id(2L)
                .build();
            productStoragePort.register(
                Product.builder()
                    .id(productId)
                    .sellerId(loginInfo.getId())
                    .deleteYn("N")
                    .build()
            );

            // when
            DeleteProductServiceResponse serviceResponse = deleteProductService
                .deleteProduct(productId, loginInfo);

            // then
            assert serviceResponse.result();
            assert productStoragePort.findByIdAndDeleteYn(productId, "N") == null;
            assert elasticSearchClientPort.database.stream()
                .noneMatch(product -> product.getId().equals(productId));
        }
    }
}
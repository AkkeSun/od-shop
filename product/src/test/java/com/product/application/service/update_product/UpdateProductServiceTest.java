package com.product.application.service.update_product;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.application.port.in.command.UpdateProductCommand;
import com.product.domain.model.Account;
import com.product.domain.model.Product;
import com.product.fakeClass.DummyMessageProducerPort;
import com.product.fakeClass.FakeProductStoragePort;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.CustomBusinessException;
import com.product.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class UpdateProductServiceTest {

    private final DummyMessageProducerPort messageProducerPort;
    private final FakeProductStoragePort productStoragePort;
    private final UpdateProductService updateProductService;

    public UpdateProductServiceTest() {
        this.messageProducerPort = new DummyMessageProducerPort();
        this.productStoragePort = new FakeProductStoragePort();
        this.updateProductService = new UpdateProductService(
            messageProducerPort, productStoragePort);
        ReflectionTestUtils.setField(updateProductService, "historyTopic", "product-history");
    }

    @Nested
    @DisplayName("[updateProduct] 상품을 수정하는 메소드")
    class Describe_updateProduct {

        @Test
        @DisplayName("[error] 상품 셀러 아이디와 인증토큰 소유자 아이다가 다를 경우 예외를 응답한다.")
        void error() {
            // given
            Long ProductId = 1L;
            Account account = Account.builder()
                .email("test@gmail.com")
                .id(2L)
                .build();
            productStoragePort.register(Product.builder()
                .id(ProductId)
                .sellerId(3L)
                .build());
            UpdateProductCommand command = UpdateProductCommand.builder()
                .productId(ProductId)
                .account(account)
                .price(1000L)
                .build();

            // when
            CustomAuthorizationException result = assertThrows(CustomAuthorizationException.class,
                () -> updateProductService.updateProduct(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.ACCESS_DENIED);
        }

        @Test
        @DisplayName("[error] 수정된 정보가 없는 경우 예외를 응답한다.")
        void error2() {
            // given
            Long ProductId = 1L;
            Account account = Account.builder()
                .email("test@gmail.com")
                .id(2L)
                .build();
            UpdateProductCommand command = UpdateProductCommand.builder()
                .productId(ProductId)
                .account(account)
                .build();
            productStoragePort.register(Product.builder()
                .id(ProductId)
                .sellerId(account.id())
                .build());

            // when
            CustomBusinessException result = assertThrows(CustomBusinessException.class,
                () -> updateProductService.updateProduct(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.Business_DoesNotExist_UPDATE_INFO);
        }

        @Test
        @DisplayName("[success] 상품이 정상적으로 수정되고 성공 데이터를 응답한다.")
        void success() {
            // given
            Long ProductId = 1L;
            Account account = Account.builder()
                .email("test@gmail.com")
                .id(2L)
                .build();
            UpdateProductCommand command = UpdateProductCommand.builder()
                .productId(ProductId)
                .account(account)
                .price(10000L)
                .build();
            productStoragePort.register(Product.builder()
                .id(ProductId)
                .sellerId(account.id())
                .build());

            // when
            UpdateProductServiceResponse response = updateProductService.updateProduct(command);

            // then
            assert response.price() == 10000L;
        }
    }
}
package com.product.application.service.Increase_product_quantity;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.application.port.in.IncreaseProductQuantityUseCase;
import com.product.application.port.in.command.IncreaseProductQuantityCommand;
import com.product.domain.model.Account;
import com.product.domain.model.Category;
import com.product.domain.model.Product;
import com.product.fakeClass.DummyMessageProducerPort;
import com.product.fakeClass.FakeProductStoragePort;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.ErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class IncreaseProductQuantityServiceTest {

    private final IncreaseProductQuantityUseCase service;
    private final FakeProductStoragePort fakeProductStoragePort;

    IncreaseProductQuantityServiceTest() {
        this.fakeProductStoragePort = new FakeProductStoragePort();
        this.service = new IncreaseProductQuantityService(
            fakeProductStoragePort,
            new DummyMessageProducerPort()
        );
        ReflectionTestUtils.setField(service, "historyTopic",
            "test-history-topic");
    }

    @Nested
    @DisplayName("[updateProductQuantity] 상품 수량을 추가하는 메소드")
    class Describe_updateProductQuantity {

        private Product createProduct(long quantity, long sellerId) {
            return Product.builder()
                .id(1L)
                .sellerId(sellerId)
                .productName("Test Product")
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .keywords(Set.of("keyword1", "keyword2"))
                .price(10000L)
                .quantity(quantity)
                .hitCount(0)
                .needsEsUpdate(false)
                .salesCount(0)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .category(Category.DIGITAL)
                .deleteYn("N")
                .regDate(LocalDate.now())
                .regDateTime(LocalDateTime.now())
                .build();
        }

        @Test
        @DisplayName("[success] 정상적으로 상품수량이 수정된다")
        void success_sufficientQuantity() {
            // given
            Product product = createProduct(100L, 1L);
            fakeProductStoragePort.database.add(product);

            IncreaseProductQuantityCommand command = IncreaseProductQuantityCommand.builder()
                .quantity(10)
                .account(Account.builder().id(1L).build())
                .build();

            // when
            IncreaseProductQuantityServiceResponse response = service.update(product.getId(),
                command);
            Product updatedProduct = fakeProductStoragePort.findByIdAndDeleteYn(1L, "N");

            // then
            assert response.result();
            assert updatedProduct.getQuantity() == 110;
            assert updatedProduct.getSalesCount() == 0;
        }

        @Test
        @DisplayName("[error] 해당 상품의 판매자가 아닌데 상품 수량을 추가하는 경우 CustomAuthorizationException 을 던진다.")
        void error() {
            // given
            Product product = createProduct(100L, 1L);
            fakeProductStoragePort.database.add(product);

            IncreaseProductQuantityCommand command = IncreaseProductQuantityCommand.builder()
                .quantity(10)
                .account(Account.builder().id(2L).build())
                .build();

            // when
            CustomAuthorizationException result = assertThrows(CustomAuthorizationException.class,
                () -> service.update(product.getId(), command));

            // then
            assert result.getErrorCode().equals(ErrorCode.ACCESS_DENIED);
        }
    }
}
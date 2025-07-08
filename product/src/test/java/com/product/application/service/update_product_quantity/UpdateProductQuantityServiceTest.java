package com.product.application.service.update_product_quantity;

import static com.product.domain.model.QuantityType.ADD_QUANTITY;
import static com.product.domain.model.QuantityType.PURCHASE;
import static com.product.domain.model.QuantityType.REFUND;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.domain.model.Account;
import com.product.domain.model.Category;
import com.product.domain.model.Product;
import com.product.fakeClass.DummyMessageProducerPort;
import com.product.fakeClass.FakeProductStoragePort;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.CustomBusinessException;
import com.product.infrastructure.exception.ErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class UpdateProductQuantityServiceTest {

    private UpdateProductQuantityService service;
    private FakeProductStoragePort fakeProductStoragePort;

    @BeforeEach
    void setUp() {
        fakeProductStoragePort = new FakeProductStoragePort();
        service = new UpdateProductQuantityService(fakeProductStoragePort,
            new DummyMessageProducerPort());
        ReflectionTestUtils.setField(service, "historyTopic",
            "test-history-topic");
    }

    @Nested
    @DisplayName("[updateProductQuantity] 상품 수량을 업데이트하는 메소드")
    class Describe_updateProductQuantity {

        private Product createProduct(long quantity, long sellerId) {
            return Product.builder()
                .id(1L)
                .sellerId(sellerId)
                .productName("Test Product")
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .productOption(Set.of("Option1", "Option2"))
                .keywords(Set.of("keyword1", "keyword2"))
                .price(10000L)
                .quantity(quantity)
                .hitCount(0)
                .needsEsUpdate(false)
                .salesCount(0)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .category(Category.ELECTRONICS)
                .deleteYn("N")
                .regDate(LocalDate.now())
                .regDateTime(LocalDateTime.now())
                .build();
        }

        @Test
        @DisplayName("[success] 상품 구매시 상품 수량이 충분하면 수량을 감소시키고 판매량을 증가시킨다.")
        void success_sufficientQuantity() {
            // given
            Product product = createProduct(100L, 1L);
            fakeProductStoragePort.database.add(product);

            UpdateProductQuantityCommand command = UpdateProductQuantityCommand.builder()
                .productId(1L)
                .quantity(10)
                .type(PURCHASE)
                .account(Account.builder().id(1L).build())
                .build();

            // when
            UpdateProductQuantityServiceResponse response = service.updateProductQuantity(
                command);
            Product updatedProduct = fakeProductStoragePort.findByIdAndDeleteYn(1L, "N");

            // then
            assert response.result();
            assert updatedProduct.getQuantity() == 90L;
            assert updatedProduct.getSalesCount() == 10L;
        }

        @Test
        @DisplayName("[error] 상품 구매시 상품 수량이 부족하면 CustomBusinessException을 던진다.")
        void error1() {
            // given
            Product product = createProduct(5L, 1L);
            fakeProductStoragePort.database.add(product);

            UpdateProductQuantityCommand command = UpdateProductQuantityCommand.builder()
                .productId(1L)
                .quantity(10)
                .type(PURCHASE)
                .account(Account.builder().id(1L).build())
                .build();

            // when
            CustomBusinessException result = assertThrows(CustomBusinessException.class,
                () -> service.updateProductQuantity(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.Business_OUT_OF_STOCK);
        }

        @Test
        @DisplayName("[success] 구매 취소시 상품 수량을 증가시키고 판매량을 감소시킨다.")
        void success2() {
            // given
            Product product = createProduct(100L, 1L);
            fakeProductStoragePort.database.add(product);

            UpdateProductQuantityCommand command = UpdateProductQuantityCommand.builder()
                .productId(1L)
                .quantity(10)
                .type(REFUND)
                .account(Account.builder().id(1L).build())
                .build();

            // when
            UpdateProductQuantityServiceResponse response = service.updateProductQuantity(
                command);
            Product updatedProduct = fakeProductStoragePort.findByIdAndDeleteYn(1L, "N");

            // then
            assert response.result();
            assert updatedProduct.getQuantity() == 110;
            assert updatedProduct.getSalesCount() == -10;
        }


        @Test
        @DisplayName("[success] 판매자가 상품 수량을 추가한다.")
        void success3() {
            // given
            Product product = createProduct(100L, 1L);
            fakeProductStoragePort.database.add(product);

            UpdateProductQuantityCommand command = UpdateProductQuantityCommand.builder()
                .productId(1L)
                .quantity(10)
                .type(ADD_QUANTITY)
                .account(Account.builder().id(1L).build())
                .build();

            // when
            UpdateProductQuantityServiceResponse response = service.updateProductQuantity(
                command);
            Product updatedProduct = fakeProductStoragePort.findByIdAndDeleteYn(1L, "N");

            // then
            assert response.result();
            assert updatedProduct.getQuantity() == 110;
            assert updatedProduct.getSalesCount() == 0;
        }

        @Test
        @DisplayName("[error] 해당 상품의 판매자가 아닌데 상품 수량을 추가하는 경우 CustomAuthorizationException을 던진다.")
        void error2() {
            // given
            Product product = createProduct(100L, 1L);
            fakeProductStoragePort.database.add(product);

            UpdateProductQuantityCommand command = UpdateProductQuantityCommand.builder()
                .productId(1L)
                .quantity(10)
                .type(ADD_QUANTITY)
                .account(Account.builder().id(2L).build())
                .build();

            // when
            CustomAuthorizationException result = assertThrows(CustomAuthorizationException.class,
                () -> service.updateProductQuantity(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.ACCESS_DENIED);
        }
    }
}

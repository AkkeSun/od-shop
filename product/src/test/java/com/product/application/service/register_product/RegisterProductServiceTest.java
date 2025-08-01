package com.product.application.service.register_product;

import static com.product.infrastructure.exception.ErrorCode.Business_ES_PRODUCT_SAVE;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.application.port.in.command.RegisterProductCommand;
import com.product.domain.model.Account;
import com.product.fakeClass.DummyGeminiClientPort;
import com.product.fakeClass.DummySnowflakeGenerator;
import com.product.fakeClass.FakeElasticSearchClientPort;
import com.product.fakeClass.FakeProductStoragePort;
import com.product.infrastructure.exception.CustomBusinessException;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterProductServiceTest {

    private final DummyGeminiClientPort geminiClientPort;
    private final DummySnowflakeGenerator snowflakeGenerator;
    private final FakeProductStoragePort productStoragePort;
    private final FakeElasticSearchClientPort elasticSearchClientPort;
    private final RegisterProductService service;

    RegisterProductServiceTest() {
        this.geminiClientPort = new DummyGeminiClientPort();
        this.snowflakeGenerator = new DummySnowflakeGenerator();
        this.productStoragePort = new FakeProductStoragePort();
        this.elasticSearchClientPort = new FakeElasticSearchClientPort();
        this.service = new RegisterProductService(geminiClientPort, snowflakeGenerator,
            productStoragePort, elasticSearchClientPort);
    }

    @AfterEach
    void tearDown() {
        productStoragePort.database.clear();
        elasticSearchClientPort.database.clear();
    }

    @Nested
    @DisplayName("[registerProduct] 상품을 등록하는 메소드")
    class Describe_registerProduct {

        @Test
        @DisplayName("[success] 상품 등록에 성공하는 경우 성공 메시지를 응답한다.")
        void success() {
            // given
            RegisterProductCommand command = RegisterProductCommand.builder()
                .account(Account.builder()
                    .id(1L)
                    .email("test@gmail.com")
                    .build())
                .productName("Test Product")
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .productOption(Set.of("Option1", "Option2"))
                .price(10000L)
                .quantity(100L)
                .category("DIGITAL")
                .build();

            // when
            RegisterProductServiceResponse response = service.registerProduct(command);

            // then
            assert response.sellerEmail().equals(command.account().email());
            assert response.productName().equals(command.productName());
            assert response.productImgUrl().equals(command.productImgUrl());
            assert response.descriptionImgUrl().equals(command.descriptionImgUrl());
            assert response.productOption().equals(command.productOption());
            assert response.price() == command.price();
            assert response.quantity() == command.quantity();
            assert response.category().name().equals(command.category());
        }

        @Test
        @DisplayName("[error] 엘라스틱서치 저장에 실패하는 경우 등록한 상품을 롤백하고 CustomBusinessException 을 응답한다.")
        void error() {
            // given
            RegisterProductCommand command = RegisterProductCommand.builder()
                .account(Account.builder()
                    .id(1L)
                    .email("test@gmail.com")
                    .build())
                .productName("error")
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .productOption(Set.of("Option1", "Option2"))
                .price(10000L)
                .quantity(100L)
                .category("DIGITAL")
                .build();

            // when
            CustomBusinessException response = assertThrows(CustomBusinessException.class,
                () -> service.registerProduct(command));

            // then
            assert productStoragePort.database.isEmpty();
            assert elasticSearchClientPort.database.isEmpty();
            assert response.getErrorCode().equals(Business_ES_PRODUCT_SAVE);
        }
    }
}
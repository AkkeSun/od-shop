package com.product.domain.model;

import com.product.application.port.in.command.RegisterProductCommand;
import com.product.application.port.in.command.UpdateProductCommand;
import com.product.application.port.in.command.UpdateProductQuantityCommand;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Nested
    @DisplayName("[of] RegisterProductCommand 와 id 로 Product 객체를 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] Product 객체를 잘 생성하는지 확인한다.")
        void success() {
            // given
            RegisterProductCommand command = RegisterProductCommand.builder()
                .account(Account.builder()
                    .id(10L)
                    .email("test@gmail.com")
                    .build())
                .productName("Test Product")
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .productOption(Set.of("Option1", "Option2"))
                .price(10000L)
                .quantity(100L)
                .category("ELECTRONICS")
                .build();
            Long id = 12L;

            // when
            Product product = Product.of(command, id);

            // then
            assert product.getId().equals(id);
            assert product.getSellerId().equals(command.account().id());
            assert product.getSellerEmail().equals(command.account().email());
            assert product.getProductName().equals(command.productName());
            assert product.getProductImgUrl().equals(command.productImgUrl());
            assert product.getDescriptionImgUrl().equals(command.descriptionImgUrl());
            assert product.getProductOption().equals(command.productOption());
            assert product.getPrice() == command.price();
            assert product.getQuantity() == command.quantity();
            assert product.getCategory().equals(Category.ELECTRONICS);
            assert product.getHitCount() == 0L;
            assert product.getSalesCount() == 0L;
            assert product.getReviewCount() == 0L;
            assert product.getReviewScore() == 0.0;
            assert product.getTotalScore() == 0.0;
            assert !product.isNeedsEsUpdate();

        }

        @Nested
        @DisplayName("[update] 상품 정보를 업데이트하는 메소드")
        class Describe_update {

            @Test
            @DisplayName("[success] 수정된 상품정보가 있는 경우 수정 목록을 응답한다.")
            void success() {
                // given
                Product product = Product.builder()
                    .productName("Old Product Name")
                    .price(10000L)
                    .build();
                UpdateProductCommand command = UpdateProductCommand.builder()
                    .productName("New Product Name")
                    .price(10000L)
                    .build();

                // when
                List<String> result = product.update(command);

                // then
                assert result.contains("productName");
                assert product.getProductName().equals("New Product Name");
            }

            @Test
            @DisplayName("[success] 수정된 상품정보가 없는 경우 빈 목록을 응답한다.")
            void success2() {
                // given
                Product product = Product.builder()
                    .productName("Old Product Name")
                    .build();
                UpdateProductCommand command = UpdateProductCommand.builder()
                    .build();

                // when
                List<String> result = product.update(command);

                // then
                assert result.isEmpty();
                assert product.getProductName().equals("Old Product Name");
            }
        }
    }

    @Nested
    @DisplayName("[updateQuantity] 상품 수량을 업데이트하는 메소드")
    class Describe_updateQuantity {

        @Test
        @DisplayName("[success] QuantityType이 REFUND인 경우 수량이 증가하고 판매량이 감소한다.")
        void refundQuantity() {
            // given
            Product product = Product.builder()
                .quantity(100L)
                .salesCount(10L)
                .build();
            UpdateProductQuantityCommand command = UpdateProductQuantityCommand.builder()
                .quantity(10)
                .type(QuantityType.REFUND)
                .build();

            // when
            product.updateQuantity(command);

            // then
            assert product.getQuantity() == 110L;
            assert product.getSalesCount() == 0L;
        }

        @Test
        @DisplayName("[success] QuantityType이 ADD_QUANTITY인 경우 수량이 증가한다.")
        void addQuantity() {
            // given
            Product product = Product.builder()
                .quantity(100L)
                .build();
            UpdateProductQuantityCommand command = UpdateProductQuantityCommand.builder()
                .quantity(10)
                .type(QuantityType.ADD_QUANTITY)
                .build();

            // when
            product.updateQuantity(command);

            // then
            assert product.getQuantity() == 110L;
        }

        @Test
        @DisplayName("[success] QuantityType이 PURCHASE인 경우 수량이 감소하고 판매량이 증가한다.")
        void purchaseQuantity() {
            // given
            Product product = Product.builder()
                .quantity(100L)
                .salesCount(10L)
                .build();
            UpdateProductQuantityCommand command = UpdateProductQuantityCommand.builder()
                .quantity(10)
                .type(QuantityType.PURCHASE)
                .build();

            // when
            product.updateQuantity(command);

            // then
            assert product.getQuantity() == 90L;
            assert product.getSalesCount() == 20L;
        }
    }
}
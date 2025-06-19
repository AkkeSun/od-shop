package com.product.domain.model;

import com.product.application.port.in.command.RegisterProductCommand;
import com.product.application.port.in.command.UpdateProductCommand;
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
                .keywords(Set.of("keyword1", "keyword2"))
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
            assert product.getKeywords().equals(command.keywords());
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

}
package com.product.application.service.register_product;

import com.product.domain.model.Product;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterProductServiceResponseTest {

    @Nested
    @DisplayName("[of] Product 객체를 RegisterProductServiceResponse로 변환하는 메소드.")
    class Describe_of {

        @Test
        @DisplayName("[success] Product 객체를 RegisterProductServiceResponse로 잘 변환하는지 확인한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(10L)
                .sellerId(1L)
                .sellerEmail("test@gmail.com")
                .productName("Test Product")
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .keywords(Set.of("keyword1", "keyword2"))
                .price(10000L)
                .quantity(100L)
                .category(Category.DIGITAL)
                .regDate(LocalDate.of(2025, 5, 1))
                .regDateTime(LocalDateTime.of(2025, 5, 1, 12, 0, 0))
                .build();

            // when
            RegisterProductServiceResponse response = RegisterProductServiceResponse.of(product);

            // then
            assert response.productId().equals(product.getId());
            assert response.sellerEmail().equals(product.getSellerEmail());
            assert response.productName().equals(product.getProductName());
            assert response.productImgUrl().equals(product.getProductImgUrl());
            assert response.descriptionImgUrl().equals(product.getDescriptionImgUrl());
            assert response.keywords().equals(product.getKeywords());
            assert response.price() == product.getPrice();
            assert response.quantity() == product.getQuantity();
            assert response.category().equals(product.getCategory());
        }
    }
}
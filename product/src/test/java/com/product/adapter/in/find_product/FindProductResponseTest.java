package com.product.adapter.in.find_product;

import com.product.application.service.find_product.FindProductServiceResponse;
import com.product.domain.model.Category;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 응답 객체로 변환한다.")
    class Describe_of{
        @Test
        @DisplayName("[success] 서비스 응답 객체를 응답 객체로 잘 변환하는지 확인한다.")
        void success () {
            // given
            FindProductServiceResponse serviceResponse = FindProductServiceResponse.builder()
                .productId(1L)
                .productName("Test Product")
                .category(Category.AUTOMOTIVE)
                .price(10000)
                .quantity(30)
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .productOption(Set.of("Option1", "Option2"))
                .keywords(Set.of("Keyword1", "Keyword2"))
                .regDateTime("2025-05-01 12:00:00")
                .build();

            // when
            FindProductResponse response = FindProductResponse.of(serviceResponse);

            // then
            assert response.productId().equals(serviceResponse.productId());
            assert response.productName().equals(serviceResponse.productName());
            assert response.category().equals(serviceResponse.category());
            assert response.price() == serviceResponse.price();
            assert response.quantity() == serviceResponse.quantity();
            assert response.productImgUrl().equals(serviceResponse.productImgUrl());
            assert response.descriptionImgUrl().equals(serviceResponse.descriptionImgUrl());
            assert response.productOption().equals(serviceResponse.productOption());
            assert response.keywords().equals(serviceResponse.keywords());
            assert response.regDateTime().equals(serviceResponse.regDateTime());
        }
    }
}
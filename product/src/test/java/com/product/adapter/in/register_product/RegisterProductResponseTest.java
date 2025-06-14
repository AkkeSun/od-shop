package com.product.adapter.in.register_product;

import com.product.application.service.register_product.RegisterProductServiceResponse;
import com.product.domain.model.Category;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterProductResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 요청 응답 객체로 변환한다.")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체를 요청 응답 객체로 잘 변환하는지 확인한다.")
        void success() {
            // given
            RegisterProductServiceResponse serviceResponse = RegisterProductServiceResponse.builder()
                .productId(12345L)
                .sellerEmail("seller")
                .productName("상품명")
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .category(Category.AUTOMOTIVE)
                .price(10000)
                .quantity(30)
                .build();

            // when
            RegisterProductResponse response = RegisterProductResponse.of(serviceResponse);

            // then
            assert response.productId().equals(serviceResponse.productId());
            assert response.sellerEmail().equals(serviceResponse.sellerEmail());
            assert response.productName().equals(serviceResponse.productName());
            assert response.productImgUrl().equals(serviceResponse.productImgUrl());
            assert response.descriptionImgUrl().equals(serviceResponse.descriptionImgUrl());
            assert response.keywords().equals(serviceResponse.keywords());
            assert response.productOption().equals(serviceResponse.productOption());
            assert response.price() == serviceResponse.price();
            assert response.quantity() == serviceResponse.quantity();
            assert response.category().equals(serviceResponse.category());
        }
    }
}
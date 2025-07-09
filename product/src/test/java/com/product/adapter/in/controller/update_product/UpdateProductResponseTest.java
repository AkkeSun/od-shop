package com.product.adapter.in.controller.update_product;

import com.product.application.service.update_product.UpdateProductServiceResponse;
import com.product.domain.model.Category;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateProductResponseTest {

    @Nested
    @DisplayName("[of] serviceResponse 를 apiResponse 로 변환한다.")
    class Describe_of {

        @Test
        @DisplayName("[success] serviceResponse 가 apiResponse 로 잘 변환하는지 확인한다.")
        void success() {
            // given
            UpdateProductServiceResponse serviceResponse = UpdateProductServiceResponse.builder()
                .productId(1L)
                .sellerEmail("sellerEmail")
                .productName("Test Product")
                .productImgUrl("http://example.com/image.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .keywords(Set.of("keyword1", "keyword2"))
                .productOption(Set.of("option1", "option2"))
                .price(1000L)
                .category(Category.TOTAL)
                .build();

            // when
            UpdateProductResponse response = UpdateProductResponse.of(serviceResponse);

            // then
            assert response.productId().equals(serviceResponse.productId());
            assert response.sellerEmail().equals(serviceResponse.sellerEmail());
            assert response.productName().equals(serviceResponse.productName());
            assert response.productImgUrl().equals(serviceResponse.productImgUrl());
            assert response.descriptionImgUrl().equals(serviceResponse.descriptionImgUrl());
            assert response.keywords().equals(serviceResponse.keywords());
            assert response.productOption().equals(serviceResponse.productOption());
            assert response.price() == serviceResponse.price();
            assert response.category().equals(serviceResponse.category().description());
        }
    }

}
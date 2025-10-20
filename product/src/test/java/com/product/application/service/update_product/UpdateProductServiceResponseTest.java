package com.product.application.service.update_product;
import com.product.domain.model.Category;

import com.product.domain.model.Product;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateProductServiceResponseTest {

    @Nested
    @DisplayName("[of] Product 객체를 UpdateProductServiceResponse로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] Product 객체를 UpdateProductServiceResponse로 변환한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(1L)
                .sellerEmail("email")
                .productName("productName")
                .productImgUrl("productImgUrl")
                .descriptionImgUrl("descriptionImgUrl")
                .keywords(Set.of("keyword1", "keyword2"))
                .price(1000L)
                .category(Category.TOTAL)
                .build();

            // when
            UpdateProductServiceResponse response = UpdateProductServiceResponse.of(product);

            // then
            assert response.productId().equals(product.getId());
            assert response.sellerEmail().equals(product.getSellerEmail());
            assert response.productName().equals(product.getProductName());
            assert response.productImgUrl().equals(product.getProductImgUrl());
            assert response.descriptionImgUrl().equals(product.getDescriptionImgUrl());
            assert response.keywords().equals(product.getKeywords());
            assert response.price() == product.getPrice();
            assert response.category().equals(product.getCategory());
            assert response.category() == Category.TOTAL;
        }
    }

}
package com.product.adapter.out.client.elasticsearch;

import com.product.domain.model.Category;
import com.product.domain.model.Product;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterProductEsRequestTest {

    @Nested
    @DisplayName("[of] Product 도메인 객체와 임베딩 벡터를 받아 RegisterProductEsRequest 를 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] Product 도메인 객체와 임베딩 벡터를 올바르게 매핑하여 RegisterProductEsRequest 를 반환한다")
        void success() {
            // given
            Product product = Product.builder()
                .id(10L)
                .productName("Test Product")
                .keywords(Set.of("test", "product"))
                .sellerEmail("email")
                .productImgUrl("http://example.com/image.jpg")
                .price(100)
                .salesCount(20)
                .reviewCount(12)
                .totalScore(20.4)
                .category(Category.FASHION)
                .regDateTime(LocalDateTime.of(2025, 10, 1, 12, 0, 0))
                .build();
            float[] embedding = new float[]{0.1f, 0.2f, 0.3f};

            // when
            RegisterProductEsRequest result = RegisterProductEsRequest.of(product, embedding);

            // then
            assert result.productId().equals(product.getId());
            assert result.productName().equals(product.getProductName());
            assert result.keywords().equals("product,test");
            assert result.sellerEmail().equals(product.getSellerEmail());
            assert result.productImgUrl().equals(product.getProductImgUrl());
            assert result.price() == product.getPrice();
            assert result.salesCount() == product.getSalesCount();
            assert result.reviewCount() == product.getReviewCount();
            assert result.totalScore() == product.getTotalScore();
            assert result.category().equals(product.getCategory().name());
            assert result.regDateTime().equals("2025-10-01 12:00:00");
            assert result.embedding().length == embedding.length;
        }
    }
}
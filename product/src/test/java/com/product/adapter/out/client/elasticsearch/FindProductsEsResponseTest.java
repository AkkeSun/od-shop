package com.product.adapter.out.client.elasticsearch;

import com.product.adapter.out.client.elasticsearch.FindProductsEsResponse.Hit.DocumentSource;
import com.product.domain.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductsEsResponseTest {

    @Nested
    @DisplayName("[toDomain] DocumentSource 를 Product 도메인 객체로 변환하는 메소드")
    class Describe_toDomain {

        @Test
        @DisplayName("[success] DocumentSource 의 필드가 올바르게 매핑되어 Product 도메인 객체를 반환한다")
        void success() {
            // given
            DocumentSource source = DocumentSource.builder()
                .productId(10L)
                .productName("Test Product")
                .keywords("test,product")
                .sellerEmail("sellerEmail")
                .price(100)
                .salesCount(20)
                .reviewCount(12)
                .totalScore(20.4)
                .category("FASHION")
                .regDateTime("2023-10-01 12:00:00")
                .embedding(new float[]{0.1f, 0.2f, 0.3f})
                .build();

            // when
            Product product = source.toDomain();

            // then
            assert product.getId() == 10L;
            assert product.getProductName().equals("Test Product");
            assert product.getKeywords().contains("test");
            assert product.getKeywords().contains("product");
            assert product.getSellerEmail().equals("sellerEmail");
            assert product.getPrice() == 100;
            assert product.getSalesCount() == 20;
            assert product.getReviewCount() == 12;
            assert product.getTotalScore() == 20.4;
            assert product.getCategory().name().equals("FASHION");
        }
    }
}
package com.product.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductMetricTest {

    @Nested
    @DisplayName("[of] Product 로 ProductMetric 객체를 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] Product 객체로 ProductMetric 객체를 잘 생성하는지 확인한다.")
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
            ProductMetric productMetric = ProductMetric.of(product);

            // then
            assert productMetric.metricId().equals(product.getId());
            assert productMetric.salesCount() == 0L;
            assert productMetric.reviewCount() == 0L;
            assert productMetric.hitCount() == 0L;
            assert productMetric.reviewScore() == 0.0;
            assert productMetric.totalScore() == 0.0;
            assert !productMetric.needsEsUpdate();
        }
    }
}
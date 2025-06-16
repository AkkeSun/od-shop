package com.product.adapter.out.persistence.jpa.shard1;

import com.product.IntegrationTestSupport;
import com.product.domain.model.Category;
import com.product.domain.model.Product;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductShard1AdapterTest extends IntegrationTestSupport {

    @Autowired
    ProductShard1Adapter adapter;

    @Autowired
    ProductShard1Repository productRepository;

    @Autowired
    ProductMetricShard1Repository metricRepository;

    @Nested
    @DisplayName("[register] 상품을 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 상품과 메트릭을 등록한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(snowflakeGenerator.nextId())
                .sellerId(1L)
                .sellerEmail("test@gmail.com")
                .productName("Test Product")
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .productOption(Set.of("Option1", "Option2"))
                .keywords(Set.of("keyword1", "keyword2"))
                .price(10000L)
                .quantity(100L)
                .category(Category.ELECTRONICS)
                .regDate(LocalDate.of(2025, 5, 1))
                .regDateTime(LocalDateTime.of(2025, 5, 1, 12, 0, 0))
                .salesCount(0L)
                .reviewCount(0L)
                .hitCount(0L)
                .reviewScore(0.0)
                .totalScore(0.0)
                .needsEsUpdate(false)
                .build();

            // when
            adapter.register(product);
            ProductShard1Entity productEntity = productRepository
                .findById(product.getId()).get();
            ProductMetricShard1Entity metricEntity = metricRepository
                .findById(product.getId()).get();

            // then
            assert productEntity.getId().equals(product.getId());
            assert productEntity.getSellerId().equals(product.getSellerId());
            assert productEntity.getProductName().equals(product.getProductName());
            assert productEntity.getProductImgUrl().equals(product.getProductImgUrl());
            assert productEntity.getDescriptionImgUrl().equals(product.getDescriptionImgUrl());
            assert productEntity.getProductOption()
                .equals(String.join(",", product.getProductOption()));
            assert productEntity.getKeyword()
                .equals(String.join(",", product.getKeywords()));
            assert productEntity.getCategory().equals(product.getCategory());
            assert productEntity.getPrice() == product.getPrice();
            assert productEntity.getQuantity() == product.getQuantity();
            assert productEntity.getRegDate().equals(product.getRegDate());
            assert productEntity.getRegDateTime().equals(product.getRegDateTime());
            assert metricEntity.getId().equals(product.getId());
            assert metricEntity.getSalesCount() == product.getSalesCount();
            assert metricEntity.getReviewCount() == product.getReviewCount();
            assert metricEntity.getHitCount() == product.getHitCount();
            assert metricEntity.getReviewScore() == product.getReviewScore();
            assert metricEntity.getTotalScore() == product.getTotalScore();
        }
    }

    @Nested
    @DisplayName("[delete] 상품을 삭제하는 메소드")
    class Describe_delete {

        @Test
        @DisplayName("[success] 상품과 메트릭을 삭제한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(snowflakeGenerator.nextId())
                .sellerId(1L)
                .sellerEmail("test@gmail.com")
                .productName("Test Product")
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .productOption(Set.of("Option1", "Option2"))
                .keywords(Set.of("keyword1", "keyword2"))
                .price(10000L)
                .quantity(100L)
                .category(Category.ELECTRONICS)
                .regDate(LocalDate.of(2025, 5, 1))
                .regDateTime(LocalDateTime.of(2025, 5, 1, 12, 0, 0))
                .salesCount(0L)
                .reviewCount(0L)
                .hitCount(0L)
                .reviewScore(0.0)
                .totalScore(0.0)
                .needsEsUpdate(false)
                .build();
            adapter.register(product);

            productRepository.findById(product.getId()).orElseThrow(
                () -> new RuntimeException("Product not found"));
            metricRepository.findById(product.getId()).orElseThrow(
                () -> new RuntimeException("Metric not found"));

            // when
            adapter.deleteById(product.getId());

            // then
            assert !productRepository.existsById(product.getId());
            assert !metricRepository.existsById(product.getId());
        }
    }
}
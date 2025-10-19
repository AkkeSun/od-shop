package com.product.adapter.out.persistence.jpa.shard1;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.IntegrationTestSupport;
import com.product.domain.model.Product;
import com.product.infrastructure.exception.CustomNotFoundException;
import com.product.infrastructure.exception.ErrorCode;
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
                .keywords(Set.of("keyword1", "keyword2"))
                .price(10000L)
                .quantity(100L)
                .category(Category.DIGITAL)
                .regDate(LocalDate.of(2025, 5, 1))
                .regDateTime(LocalDateTime.of(2025, 5, 1, 12, 0, 0))
                .salesCount(0L)
                .reviewCount(0L)
                .hitCount(0L)
                .reviewScore(0.0)
                .totalScore(0.0)
                .deleteYn("N")
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
                .keywords(Set.of("keyword1", "keyword2"))
                .price(10000L)
                .quantity(100L)
                .category(Category.DIGITAL)
                .regDate(LocalDate.of(2025, 5, 1))
                .regDateTime(LocalDateTime.of(2025, 5, 1, 12, 0, 0))
                .salesCount(0L)
                .reviewCount(0L)
                .hitCount(0L)
                .reviewScore(0.0)
                .totalScore(0.0)
                .deleteYn("N")
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

    @Nested
    @DisplayName("[findByIdAndDeleteYn] 아이디와 삭제 유무로 상품을 조회하는 메소드")
    class Describe_findById {

        @Test
        @DisplayName("[success] 조회된 상품이 존재한다면 상품을 반환한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(snowflakeGenerator.nextId())
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
                .salesCount(0L)
                .reviewCount(0L)
                .hitCount(0L)
                .reviewScore(0.0)
                .totalScore(0.0)
                .needsEsUpdate(false)
                .deleteYn("N")
                .build();
            adapter.register(product);

            // when
            Product result = adapter.findByIdAndDeleteYn(product.getId(), "N");

            // then
            assert result.getId().equals(product.getId());
            assert result.getSellerId().equals(product.getSellerId());
            assert result.getProductName().equals(product.getProductName());
            assert result.getProductImgUrl().equals(product.getProductImgUrl());
            assert result.getDescriptionImgUrl().equals(product.getDescriptionImgUrl());
            assert result.getKeywords().equals(product.getKeywords());
            assert result.getCategory().equals(product.getCategory());
            assert result.getPrice() == product.getPrice();
            assert result.getQuantity() == product.getQuantity();
            assert result.getRegDate().equals(product.getRegDate());
            assert result.getRegDateTime().equals(product.getRegDateTime());
        }

        @Test
        @DisplayName("[error] 조회된 상품이 존재하지 않는다면 예외를 발생시킨다.")
        void error() {
            // given
            Long productId = -1L;

            // when
            CustomNotFoundException result = assertThrows(
                CustomNotFoundException.class, () -> {
                    adapter.findByIdAndDeleteYn(productId, "N");
                });

            // then
            assert result.getErrorCode().equals(ErrorCode.DoesNotExist_PRODUCT_INFO);
        }

        @Test
        @DisplayName("[success] 삭제 유무를 A 로 입력할 경우 삭제 유무와 상관없이 상품을 조회한다.")
        void success2() {
            // given
            Product product = Product.builder()
                .id(snowflakeGenerator.nextId())
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
                .salesCount(0L)
                .reviewCount(0L)
                .hitCount(0L)
                .reviewScore(0.0)
                .totalScore(0.0)
                .needsEsUpdate(false)
                .deleteYn("N")
                .build();
            adapter.register(product);
            Product product2 = Product.builder()
                .id(snowflakeGenerator.nextId())
                .sellerId(2L)
                .sellerEmail("test2@gmail.com")
                .productName("Test Product2")
                .productImgUrl("http://example.com/product2.jpg")
                .descriptionImgUrl("http://example.com/description2.jpg")
                .keywords(Set.of("keyword3", "keyword4"))
                .price(10000L)
                .quantity(100L)
                .category(Category.DIGITAL)
                .regDate(LocalDate.of(2025, 5, 1))
                .regDateTime(LocalDateTime.of(2025, 5, 1, 12, 0, 0))
                .salesCount(0L)
                .reviewCount(0L)
                .hitCount(0L)
                .reviewScore(0.0)
                .totalScore(0.0)
                .needsEsUpdate(false)
                .deleteYn("Y")
                .build();
            adapter.register(product2);

            // when
            Product result = adapter.findByIdAndDeleteYn(product.getId(), "A");
            Product result2 = adapter.findByIdAndDeleteYn(product2.getId(), "A");

            // then
            assert result.getId().equals(product.getId());
            assert result.getSellerId().equals(product.getSellerId());
            assert result.getProductName().equals(product.getProductName());
            assert result.getProductImgUrl().equals(product.getProductImgUrl());
            assert result.getDescriptionImgUrl().equals(product.getDescriptionImgUrl());
            assert result.getKeywords().equals(product.getKeywords());
            assert result.getCategory().equals(product.getCategory());
            assert result.getPrice() == product.getPrice();
            assert result.getQuantity() == product.getQuantity();
            assert result.getRegDate().equals(product.getRegDate());
            assert result.getRegDateTime().equals(product.getRegDateTime());
            assert result2.getId().equals(product2.getId());
            assert result2.getSellerId().equals(product2.getSellerId());
            assert result2.getProductName().equals(product2.getProductName());
            assert result2.getProductImgUrl().equals(product2.getProductImgUrl());
            assert result2.getDescriptionImgUrl().equals(product2.getDescriptionImgUrl());
            assert result2.getKeywords().equals(product2.getKeywords());
            assert result2.getCategory().equals(product2.getCategory());
            assert result2.getPrice() == product2.getPrice();
            assert result2.getQuantity() == product2.getQuantity();
            assert result2.getRegDate().equals(product2.getRegDate());
            assert result2.getRegDateTime().equals(product2.getRegDateTime());
        }
    }

    @Nested
    @DisplayName("[softDeleteById] 상품을 소프트 삭제하는 메소드")
    class Describe_softDeleteById {

        @Test
        @DisplayName("[success] 상품 메트릭을 삭제하고 상품 정보를 소프트 삭제한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(snowflakeGenerator.nextId())
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
                .salesCount(0L)
                .reviewCount(0L)
                .hitCount(0L)
                .reviewScore(0.0)
                .totalScore(0.0)
                .needsEsUpdate(false)
                .deleteYn("N")
                .build();
            adapter.register(product);
            LocalDateTime deleteAt = LocalDateTime.of(2025, 5, 2, 12, 0, 0);

            // when
            adapter.softDeleteById(product.getId(), deleteAt);
            CustomNotFoundException result = assertThrows(
                CustomNotFoundException.class,
                () -> adapter.findByIdAndDeleteYn(product.getId(), "N"));
            ProductShard1Entity entity = productRepository.findById(product.getId()).get();

            // then
            assert result.getErrorCode().equals(ErrorCode.DoesNotExist_PRODUCT_INFO);
            assert entity.getDeleteYn().equals("Y");
            assert entity.getUpdateDateTime().equals(deleteAt);
            assert metricRepository.findById(product.getId()).isEmpty();
        }
    }
}
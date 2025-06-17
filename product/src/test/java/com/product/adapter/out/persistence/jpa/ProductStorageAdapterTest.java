package com.product.adapter.out.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.IntegrationTestSupport;
import com.product.adapter.out.persistence.jpa.shard1.ProductShard1Adapter;
import com.product.adapter.out.persistence.jpa.shard2.ProductShard2Adapter;
import com.product.domain.model.Category;
import com.product.domain.model.Product;
import com.product.infrastructure.exception.CustomNotFoundException;
import com.product.infrastructure.exception.ErrorCode;
import com.product.infrastructure.util.ShardKeyUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductStorageAdapterTest extends IntegrationTestSupport {

    @Autowired
    ProductStorageAdapter adapter;
    @Autowired
    ProductShard1Adapter shard1Adapter;
    @Autowired
    ProductShard2Adapter shard2Adapter;

    @Nested
    @DisplayName("[register] 상품을 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 상품 아이디가 shard1에 해당하면 shard1에 상품 정보를 저장한다.")
        void success1() {
            // given
            Product product = Product.builder()
                .id(getProductId(true))
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
            Product result = shard1Adapter.findById(product.getId());

            // then
            assert result.getId().equals(product.getId());
            assert result.getSellerId().equals(product.getSellerId());
            assert result.getProductName().equals(product.getProductName());
            assert result.getProductImgUrl().equals(product.getProductImgUrl());
            assert result.getDescriptionImgUrl().equals(product.getDescriptionImgUrl());
            assert result.getProductOption().equals(product.getProductOption());
            assert result.getKeywords().equals(product.getKeywords());
            assert result.getCategory().equals(product.getCategory());
            assert result.getPrice() == product.getPrice();
            assert result.getQuantity() == product.getQuantity();
            assert result.getRegDate().equals(product.getRegDate());
            assert result.getRegDateTime().equals(product.getRegDateTime());

            // clean
            adapter.deleteById(product.getId());
        }

        @Test
        @DisplayName("[success] 상품 아이디가 shard2에 해당하면 shard2에 상품 정보를 저장한다.")
        void success2() {
            // given
            Product product = Product.builder()
                .id(getProductId(false))
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
            Product result = shard2Adapter.findById(product.getId());

            // then
            assert result.getId().equals(product.getId());
            assert result.getSellerId().equals(product.getSellerId());
            assert result.getProductName().equals(product.getProductName());
            assert result.getProductImgUrl().equals(product.getProductImgUrl());
            assert result.getDescriptionImgUrl().equals(product.getDescriptionImgUrl());
            assert result.getProductOption().equals(product.getProductOption());
            assert result.getKeywords().equals(product.getKeywords());
            assert result.getCategory().equals(product.getCategory());
            assert result.getPrice() == product.getPrice();
            assert result.getQuantity() == product.getQuantity();
            assert result.getRegDate().equals(product.getRegDate());
            assert result.getRegDateTime().equals(product.getRegDateTime());

            // clean
            adapter.deleteById(product.getId());
        }
    }

    @Nested
    @DisplayName("[delete] 상품을 삭제하는 메소드")
    class Describe_delete {

        @Test
        @DisplayName("[success] 상품 아이디가 shard1에 해당하면 shard1에 상품 정보를 삭제한다.")
        void success1() {
            // given
            Product product = Product.builder()
                .id(getProductId(true))
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
            Product savedProduct = shard1Adapter.findById(product.getId());
            assert savedProduct != null;

            // when
            adapter.deleteById(product.getId());
            CustomNotFoundException result = assertThrows(CustomNotFoundException.class,
                () -> shard1Adapter.findById(product.getId()));

            // then
            assert result.getErrorCode().equals(ErrorCode.DoesNotExist_PROUCT_INFO);
        }

        @Test
        @DisplayName("[success] 상품 아이디가 shard2에 해당하면 shard2에 상품 정보를 삭제한다.")
        void success2() {
            // given
            Product product = Product.builder()
                .id(getProductId(false))
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
            Product savedProduct = shard2Adapter.findById(product.getId());
            assert savedProduct != null;

            // when
            adapter.deleteById(product.getId());
            CustomNotFoundException result = assertThrows(CustomNotFoundException.class,
                () -> shard2Adapter.findById(product.getId()));

            // then
            assert result.getErrorCode().equals(ErrorCode.DoesNotExist_PROUCT_INFO);
        }

        @Nested
        @DisplayName("[findById] 아이디로 상품을 조회하는 메소드")
        class Describe_findById {

            @Test
            @DisplayName("[success] 상품 아이디가 shard1에 해당하면 shard1에서 상품을 조회한다.")
            void success1() {
                // given
                Product product = Product.builder()
                    .id(getProductId(true))
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
                shard1Adapter.register(product);

                // when
                Product result = adapter.findById(product.getId());

                // then
                assert result.getId().equals(product.getId());
                assert result.getSellerId().equals(product.getSellerId());
                assert result.getProductName().equals(product.getProductName());
                assert result.getProductImgUrl().equals(product.getProductImgUrl());
                assert result.getDescriptionImgUrl().equals(product.getDescriptionImgUrl());
                assert result.getProductOption().equals(product.getProductOption());
                assert result.getKeywords().equals(product.getKeywords());
                assert result.getCategory().equals(product.getCategory());
                assert result.getPrice() == product.getPrice();
                assert result.getQuantity() == product.getQuantity();
                assert result.getRegDate().equals(product.getRegDate());
                assert result.getRegDateTime().equals(product.getRegDateTime());

                // clean
                shard1Adapter.deleteById(product.getId());
            }

            @Test
            @DisplayName("[success] 상품 아이디가 shard2에 해당하면 shard2에서 상품을 조회한다.")
            void success2() {
                // given
                Product product = Product.builder()
                    .id(getProductId(false))
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
                shard2Adapter.register(product);

                // when
                Product result = adapter.findById(product.getId());

                // then
                assert result.getId().equals(product.getId());
                assert result.getSellerId().equals(product.getSellerId());
                assert result.getProductName().equals(product.getProductName());
                assert result.getProductImgUrl().equals(product.getProductImgUrl());
                assert result.getDescriptionImgUrl().equals(product.getDescriptionImgUrl());
                assert result.getProductOption().equals(product.getProductOption());
                assert result.getKeywords().equals(product.getKeywords());
                assert result.getCategory().equals(product.getCategory());
                assert result.getPrice() == product.getPrice();
                assert result.getQuantity() == product.getQuantity();
                assert result.getRegDate().equals(product.getRegDate());
                assert result.getRegDateTime().equals(product.getRegDateTime());

                // clean
                shard2Adapter.deleteById(product.getId());
            }
        }
    }

    private Long getProductId(boolean isShard1) {
        if (isShard1) {
            while (true) {
                Long productId = snowflakeGenerator.nextId();
                if (ShardKeyUtil.isShard1(productId)) {
                    return productId;
                }
            }
        }

        while (true) {
            Long productId = snowflakeGenerator.nextId();
            if (!ShardKeyUtil.isShard1(productId)) {
                return productId;
            }
        }
    }
}
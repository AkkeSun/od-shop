package com.product.adapter.out.persistence.elasticSearch;

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

// TODO: prod, test 환경에서 ElasticSearch 설정을 분리하여 테스트 환경에서만 실행되도록 조정 필요
class ProductEsStorageAdapterTest extends IntegrationTestSupport {

    @Autowired
    ProductEsStorageAdapter adapter;

    @Autowired
    ProductEsRepository repository;

    @Nested
    @DisplayName("[register] 상품을 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 상품이 잘 등록되는지 확인한다.")
        void success() {
            // given
            Product product = getProduct();
            float[] embedding = embeddingUtil.embedToFloatArray(product.getEmbeddingDocument());

            // when
            adapter.register(product, embedding);
            ProductEsDocument productEsDocument = repository
                .findById(product.getId()).orElseThrow();

            // then
            assert productEsDocument.getProductId().equals(product.getId());
            assert productEsDocument.getProductName().equals(product.getProductName());
            assert productEsDocument.getSellerEmail().equals(product.getSellerEmail());
            assert productEsDocument.getProductImgUrl().equals(product.getProductImgUrl());
            assert productEsDocument.getPrice() == product.getPrice();
            assert productEsDocument.getKeywords().equals(String.join(",", product.getKeywords()));
            assert productEsDocument.getCategory().equals(product.getCategory());
            assert productEsDocument.getReviewCount() == product.getReviewCount();
            assert productEsDocument.getTotalScore() == product.getTotalScore();
            for (int i = 0; i < embedding.length; i++) {
                assert productEsDocument.getEmbedding()[i] == embedding[i];
            }

            // clean
            repository.deleteById(product.getId());
        }

        @Nested
        @DisplayName("[deleteById] 상품을 삭제하는 메소드")
        class Describe_deleteById {

            @Test
            @DisplayName("[success] 상품이 잘 삭제되는지 확인한다.")
            void success() {
                // given
                Product product = getProduct();
                float[] embedding = embeddingUtil.embedToFloatArray(product.getEmbeddingDocument());
                adapter.register(product, embedding);
                repository.findById(product.getId()).orElseThrow();
                
                // when
                adapter.deleteById(product.getId());

                // then
                assert repository.findById(product.getId()).isEmpty();
            }
        }
    }

    private Product getProduct() {
        return Product.builder()
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
            .deleteYn("N")
            .needsEsUpdate(false)
            .build();
    }
}
package com.product.application.service.find_product;

import com.product.domain.model.Category;
import com.product.domain.model.Product;
import com.product.fakeClass.FakeProductStoragePort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductServiceTest {

    private final FakeProductStoragePort productStoragePort;
    private final FindProductService service;

    public FindProductServiceTest() {
        this.productStoragePort = new FakeProductStoragePort();
        this.service = new FindProductService(productStoragePort);
    }

    @AfterEach
    void tearDown() {
        productStoragePort.database.clear();
    }

    @Nested
    @DisplayName("[findProduct] 상품을 조회한다.")
    class Describe_findProduct {
        @Test
        @DisplayName("[success] 상품 ID로 상품을 잘 조회하는지 확인한다.")
        void success () {
            // given
            Product product = Product.builder()
                .id(20L)
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
                .build();
            productStoragePort.database.add(product);

            // when
            FindProductServiceResponse response = service.findProduct(product.getId());

            // then
            assert response.productId().equals(product.getId());
            assert response.productName().equals(product.getProductName());
            assert response.category().equals(product.getCategory());
            assert response.price() == product.getPrice();
            assert response.quantity() == product.getQuantity();
            assert response.productImgUrl().equals(product.getProductImgUrl());
            assert response.descriptionImgUrl().equals(product.getDescriptionImgUrl());
            assert response.productOption().equals(product.getProductOption());
            assert response.keywords().equals(product.getKeywords());
        }
    }
}
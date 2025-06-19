package com.product.domain.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductHistoryTest {

    @Nested
    @DisplayName("[createProductHistoryForDelete] 상품 삭제 이력을 생성하는 메소드")
    class Describe_createProductHistoryForDelete {

        @Test
        @DisplayName("[success] 상품 삭제 이력을 성공적으로 생성한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(1L)
                .sellerId(2L)
                .productName("Test Product")
                .build();
            LocalDateTime deleteAt = LocalDateTime.of(2023, 10, 1, 12, 0);

            // when
            ProductHistory productHistory = ProductHistory.createProductHistoryForDelete(product,
                deleteAt);

            // then
            assert productHistory.productId().equals(1L);
            assert productHistory.accountId().equals(2L);
            assert productHistory.type().equals("delete");
            assert productHistory.detailInfo().equals("Test Product");
            assert productHistory.regDateTime()
                .equals(deleteAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }

    @Nested
    @DisplayName("[createProductHistoryForUpdate] 상품 수정 이력을 생성하는 메소드")
    class createProductHistoryForUpdate {

        @Test
        @DisplayName("[success] 상품 수정 이력을 성공적으로 생성한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(1L)
                .sellerId(2L)
                .productName("Test Product")
                .updateDateTime(LocalDateTime.of(2025, 10, 1, 12, 0))
                .build();
            List<String> updateList = List.of("price", "description");

            // when
            ProductHistory result = ProductHistory.createProductHistoryForUpdate(
                product, updateList);

            // then
            assert result.productId().equals(1L);
            assert result.accountId().equals(2L);
            assert result.type().equals("update");
            assert result.detailInfo().equals("price,description");
            assert result.regDateTime().equals("2025-10-01 12:00:00");
        }
    }
}
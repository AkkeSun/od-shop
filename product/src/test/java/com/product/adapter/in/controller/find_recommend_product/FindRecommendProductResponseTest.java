package com.product.adapter.in.controller.find_recommend_product;

import com.product.application.service.find_recommend_product.FindRecommendProductServiceResponse;
import com.product.domain.model.ProductRecommend;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindRecommendProductResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객채를 API 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[of] 서비스 응답 객채를 API 응답 객체로 잘 변환하는지 확인한다.")
        void success() {
            // given
            FindRecommendProductServiceResponse serviceResponse = FindRecommendProductServiceResponse.builder()
                .personallyList(Collections.singletonList(
                    ProductRecommend.builder()
                        .productId(10L)
                        .price(10000)
                        .productName("product1")
                        .sellerEmail("seller1")
                        .productImgUrl("img1")
                        .build()))
                .popularList(Collections.singletonList(
                    ProductRecommend.builder()
                        .productId(11L)
                        .price(11000)
                        .productName("product2")
                        .sellerEmail("seller2")
                        .productImgUrl("img2")
                        .build()))
                .trendList(Collections.singletonList(
                    ProductRecommend.builder()
                        .productId(12L)
                        .price(12000)
                        .productName("product3")
                        .sellerEmail("seller3")
                        .productImgUrl("img3")
                        .build()))
                .build();

            // when
            FindRecommendProductResponse result = FindRecommendProductResponse.of(serviceResponse);

            // then
            assert result.personallyList().getFirst().productId().equals(10L);
            assert result.personallyList().getFirst().price() == 10000;
            assert result.personallyList().getFirst().productName().equals("product1");
            assert result.personallyList().getFirst().sellerEmail().equals("seller1");
            assert result.personallyList().getFirst().productImgUrl().equals("img1");
            assert result.popularList().getFirst().productId().equals(11L);
            assert result.popularList().getFirst().price() == 11000;
            assert result.popularList().getFirst().productName().equals("product2");
            assert result.popularList().getFirst().sellerEmail().equals("seller2");
            assert result.popularList().getFirst().productImgUrl().equals("img2");
            assert result.trendList().getFirst().productId().equals(12L);
            assert result.trendList().getFirst().price() == 12000;
            assert result.trendList().getFirst().productName().equals("product3");
            assert result.trendList().getFirst().sellerEmail().equals("seller3");
            assert result.trendList().getFirst().productImgUrl().equals("img3");
        }
    }
}
package com.product.adapter.in.controller.find_product_list;

import com.product.application.service.find_product_list.FindProductListServiceResponse;
import com.product.application.service.find_product_list.FindProductListServiceResponse.FindProductListServiceResponseItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductListResponseTest {

    @Nested
    @DisplayName("[of] serviceResponse 를 ApiResponse 로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] serviceResponse 를 ApiResponse 로 잘 변환하는지 확인한다.")
        void success() {
            // given
            FindProductListServiceResponse serviceResponse = FindProductListServiceResponse.builder()
                .page(0)
                .size(10)
                .productCount(2)
                .productList(List.of(
                    FindProductListServiceResponseItem.builder()
                        .id(123L)
                        .productName("product1")
                        .sellerEmail("seller1")
                        .productImgUrl("img1")
                        .price(100000)
                        .build(),
                    FindProductListServiceResponseItem.builder()
                        .id(234L)
                        .productName("product2")
                        .sellerEmail("seller2")
                        .productImgUrl("img2")
                        .price(200000)
                        .build()))
                .build();

            // when
            FindProductListResponse result = FindProductListResponse.of(serviceResponse);

            // then
            assert result.page() == serviceResponse.page();
            assert result.size() == serviceResponse.size();
            assert result.productCount() == serviceResponse.productCount();
            assert result.productList().getFirst().id().equals(123L);
            assert result.productList().getFirst().productName().equals("product1");
            assert result.productList().getFirst().sellerEmail().equals("seller1");
            assert result.productList().getFirst().productImgUrl().equals("img1");
            assert result.productList().getFirst().price() == 100000;
            assert result.productList().getLast().id().equals(234L);
            assert result.productList().getLast().productName().equals("product2");
            assert result.productList().getLast().sellerEmail().equals("seller2");
            assert result.productList().getLast().productImgUrl().equals("img2");
            assert result.productList().getLast().price() == 200000;
        }
    }
}
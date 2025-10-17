package com.order.adapter.in.controller.find_sold_products;

import com.order.adapter.in.controller.find_sold_products.FindSoldProductsResponse.FindSellerListItem;
import com.order.application.service.find_sold_products.FindSoldProductsServiceResponse;
import com.order.application.service.find_sold_products.FindSoldProductsServiceResponse.FindSoldProductsServiceResponseItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindSoldProductsResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답을 API 응답으로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답을 API 응답으로 잘 변환하는지 확인한다")
        void success() {
            // given
            FindSoldProductsServiceResponseItem item = FindSoldProductsServiceResponseItem.builder()
                .orderProductId(50L)
                .customerId(60L)
                .productName("productName")
                .productPrice(10)
                .buyQuantity(10)
                .buyStatus("buyStatus")
                .regDateTime("regDateTime")
                .build();
            FindSoldProductsServiceResponse serviceResponse = FindSoldProductsServiceResponse.builder()
                .pageNumber(10)
                .pageSize(20)
                .totalElements(30)
                .totalPages(40)
                .orderList(List.of(item))
                .build();

            // when
            FindSoldProductsResponse response = FindSoldProductsResponse.of(serviceResponse);

            // then
            assert response.pageNumber() == serviceResponse.pageNumber();
            assert response.pageSize() == serviceResponse.pageSize();
            assert response.totalElements() == serviceResponse.totalElements();
            assert response.totalPages() == serviceResponse.totalPages();
            assert response.orderList().size() == 1;
            FindSellerListItem first = response.orderList().getFirst();
            assert first.orderProductId().equals(item.orderProductId());
            assert first.customerId().equals(item.customerId());
            assert first.productName().equals(item.productName());
            assert first.productPrice() == item.productPrice();
            assert first.buyQuantity() == item.buyQuantity();
            assert first.buyStatus().equals(item.buyStatus());
            assert first.regDateTime().equals(item.regDateTime());
        }
    }
}
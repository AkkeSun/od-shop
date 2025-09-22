package com.order.adapter.in.controller.find_customer_orders;

import com.order.adapter.in.controller.find_customer_orders.FindCustomerOrdersResponse.FindOrderListItem;
import com.order.applicatoin.service.find_customer_orders.FindCustomerOrdersServiceResponse;
import com.order.applicatoin.service.find_customer_orders.FindCustomerOrdersServiceResponse.FindCustomerOrdersServiceResponseItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindCustomerOrdersResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답을 API 응답으로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답을 API 응답으로 잘 변환하는지 확인한다")
        void success() {
            // given
            FindCustomerOrdersServiceResponseItem item = FindCustomerOrdersServiceResponseItem.builder()
                .orderNumber(50L)
                .orderDateTime("orderDateTime")
                .primaryProductName("primaryProductName")
                .primaryProductImg("primaryProductImg")
                .primaryProductBuyStatus("primaryProductBuyStatus")
                .totalProductCnt(1)
                .totalPrice(10)
                .build();
            FindCustomerOrdersServiceResponse serviceResponse = FindCustomerOrdersServiceResponse.builder()
                .pageNumber(10)
                .pageSize(20)
                .totalElements(30)
                .totalPages(40)
                .orderList(List.of(item))
                .build();

            // when
            FindCustomerOrdersResponse response = FindCustomerOrdersResponse.of(
                serviceResponse);

            // then
            assert response.pageNumber() == serviceResponse.pageNumber();
            assert response.pageSize() == serviceResponse.pageSize();
            assert response.totalElements() == serviceResponse.totalElements();
            assert response.totalPages() == serviceResponse.totalPages();
            assert response.orderList().size() == 1;
            FindOrderListItem first = response.orderList().getFirst();
            assert first.orderNumber().equals(item.orderNumber());
            assert first.orderDateTime().equals(item.orderDateTime());
            assert first.primaryProductImg().equals(item.primaryProductImg());
            assert first.primaryProductName().equals(item.primaryProductName());
            assert first.primaryProductBuyStatus().equals(item.primaryProductBuyStatus());
            assert first.totalProductCnt() == item.totalProductCnt();
            assert first.totalPrice() == item.totalPrice();
        }
    }
}
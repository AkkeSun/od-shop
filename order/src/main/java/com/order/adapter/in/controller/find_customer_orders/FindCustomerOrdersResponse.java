package com.order.adapter.in.controller.find_customer_orders;

import com.order.applicatoin.service.find_customer_orders.FindCustomerOrdersServiceResponse;
import com.order.applicatoin.service.find_customer_orders.FindCustomerOrdersServiceResponse.FindCustomerOrdersServiceResponseItem;
import java.util.List;
import lombok.Builder;

@Builder
record FindCustomerOrdersResponse(
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    List<FindOrderListItem> orderList
) {

    static FindCustomerOrdersResponse of(FindCustomerOrdersServiceResponse serviceResponse) {
        return FindCustomerOrdersResponse.builder()
            .pageNumber(serviceResponse.pageNumber())
            .pageSize(serviceResponse.pageSize())
            .totalElements(serviceResponse.totalElements())
            .totalPages(serviceResponse.totalPages())
            .orderList(serviceResponse.orderList().stream()
                .map(FindOrderListItem::of)
                .toList())
            .build();
    }

    @Builder
    record FindOrderListItem(
        Long orderNumber,
        String orderDateTime,
        String primaryProductName,
        String primaryProductImg,
        long totalProductCnt,
        long totalPrice,
        String buyStatus

    ) {

        public static FindOrderListItem of(FindCustomerOrdersServiceResponseItem item) {
            return FindOrderListItem.builder()
                .orderNumber(item.orderNumber())
                .orderDateTime(item.orderDateTime())
                .primaryProductName(item.primaryProductName())
                .primaryProductImg(item.primaryProductImg())
                .totalProductCnt(item.totalProductCnt())
                .totalPrice(item.totalPrice())
                .buyStatus(item.buyStatus())
                .build();
        }
    }
}
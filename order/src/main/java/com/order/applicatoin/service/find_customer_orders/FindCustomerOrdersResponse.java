package com.order.applicatoin.service.find_customer_orders;

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

    }
}
package com.order.applicatoin.service.find_customer_orders;

import java.util.List;
import lombok.Builder;

public record FindCustomerOrdersServiceResponse(
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    List<FindCustomerOrdersServiceResponseItem> orderList
) {

    @Builder
    record FindCustomerOrdersServiceResponseItem(
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

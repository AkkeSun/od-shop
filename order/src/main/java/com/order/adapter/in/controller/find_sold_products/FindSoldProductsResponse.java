package com.order.adapter.in.controller.find_sold_products;

import java.util.List;
import lombok.Builder;

@Builder
record FindSoldProductsResponse(
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    List<FindSellerListItem> orderList
) {


    @Builder
    record FindSellerListItem(
        Long orderProductId,
        Long customerId,
        String productName,
        long productPrice,
        long buyQuantity,
        String buyStatus,
        String regDateTime
    ) {

    }
}
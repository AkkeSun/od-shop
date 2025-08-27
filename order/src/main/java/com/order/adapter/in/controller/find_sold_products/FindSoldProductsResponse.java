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
        Long productNumber,
        Long customerId,
        String productName,
        long productPrice,
        long buyQuantity,
        String buyStatus
    ) {

    }
}
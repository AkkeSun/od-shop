package com.order.adapter.in.controller.find_sold_products;

import com.order.application.service.find_sold_products.FindSoldProductsServiceResponse;
import com.order.application.service.find_sold_products.FindSoldProductsServiceResponse.FindSoldProductsServiceResponseItem;
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


    static FindSoldProductsResponse of(FindSoldProductsServiceResponse serviceResponse) {
        return FindSoldProductsResponse.builder()
            .pageNumber(serviceResponse.pageNumber())
            .pageSize(serviceResponse.pageSize())
            .totalElements(serviceResponse.totalElements())
            .totalPages(serviceResponse.totalPages())
            .orderList(serviceResponse.orderList().stream()
                .map(FindSellerListItem::of)
                .toList())
            .build();
    }

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

        public static FindSellerListItem of(FindSoldProductsServiceResponseItem item) {
            return FindSellerListItem.builder()
                .orderProductId(item.orderProductId())
                .customerId(item.customerId())
                .productName(item.productName())
                .productPrice(item.productPrice())
                .buyQuantity(item.buyQuantity())
                .buyStatus(item.buyStatus())
                .regDateTime(item.regDateTime())
                .build();
        }
    }
}
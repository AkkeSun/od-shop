package com.product.adapter.in.controller.find_product_list;

import com.product.application.service.find_product_list.FindProductListServiceResponse;
import lombok.Builder;

@Builder
record FindProductListResponse(
    Long id,
    String productName,
    String sellerEmail,
    String productImgUrl,
    long price
) {

    static FindProductListResponse of(FindProductListServiceResponse serviceResponse) {
        return FindProductListResponse.builder()
            .id(serviceResponse.id())
            .productName(serviceResponse.productName())
            .sellerEmail(serviceResponse.sellerEmail())
            .productImgUrl(serviceResponse.productImgUrl())
            .price(serviceResponse.price())
            .build();
    }
}

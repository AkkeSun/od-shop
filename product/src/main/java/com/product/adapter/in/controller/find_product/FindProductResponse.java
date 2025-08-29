package com.product.adapter.in.controller.find_product;

import com.product.application.service.find_product.FindProductServiceResponse;
import java.util.Set;
import lombok.Builder;

@Builder
record FindProductResponse(
    Long productId,
    String sellerEmail,
    String productName,
    String productImgUrl,
    String descriptionImgUrl,
    Set<String> keywords,
    long price,
    long quantity,
    String category,
    String regDateTime
) {

    static FindProductResponse of(FindProductServiceResponse serviceResponse) {
        return FindProductResponse.builder()
            .productId(serviceResponse.productId())
            .sellerEmail(serviceResponse.sellerEmail())
            .productName(serviceResponse.productName())
            .productImgUrl(serviceResponse.productImgUrl())
            .descriptionImgUrl(serviceResponse.descriptionImgUrl())
            .keywords(serviceResponse.keywords())
            .price(serviceResponse.price())
            .quantity(serviceResponse.quantity())
            .category(serviceResponse.category().description())
            .regDateTime(serviceResponse.regDateTime())
            .build();
    }
}

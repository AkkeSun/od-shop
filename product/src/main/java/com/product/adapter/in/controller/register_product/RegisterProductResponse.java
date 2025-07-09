package com.product.adapter.in.controller.register_product;

import com.product.application.service.register_product.RegisterProductServiceResponse;
import java.util.Set;
import lombok.Builder;

@Builder
record RegisterProductResponse(
    Long productId,
    String sellerEmail,
    String productName,
    String productImgUrl,
    Set<String> keywords,
    Set<String> productOption,
    String descriptionImgUrl,
    long price,
    long quantity,
    String category
) {


    static RegisterProductResponse of(RegisterProductServiceResponse serviceResponse) {
        return RegisterProductResponse.builder()
            .productId(serviceResponse.productId())
            .sellerEmail(serviceResponse.sellerEmail())
            .productName(serviceResponse.productName())
            .productImgUrl(serviceResponse.productImgUrl())
            .descriptionImgUrl(serviceResponse.descriptionImgUrl())
            .keywords(serviceResponse.keywords())
            .productOption(serviceResponse.productOption())
            .price(serviceResponse.price())
            .quantity(serviceResponse.quantity())
            .category(serviceResponse.category().description())
            .build();
    }
}

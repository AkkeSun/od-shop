package com.product.adapter.in.find_product;

import com.product.application.service.find_product.FindProductServiceResponse;
import com.product.domain.model.Category;
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
    Set<String> productOption,
    long price,
    long quantity,
    Category category,
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
            .productOption(serviceResponse.productOption())
            .price(serviceResponse.price())
            .quantity(serviceResponse.quantity())
            .category(serviceResponse.category())
            .regDateTime(serviceResponse.regDateTime())
            .build();
    }
}

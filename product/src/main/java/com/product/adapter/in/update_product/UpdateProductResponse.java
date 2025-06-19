package com.product.adapter.in.update_product;

import com.product.application.service.update_product.UpdateProductServiceResponse;
import com.product.domain.model.Category;
import java.util.Set;
import lombok.Builder;

@Builder
record UpdateProductResponse(
    Long productId,
    String sellerEmail,
    String productName,
    String productImgUrl,
    Set<String> keywords,
    Set<String> productOption,
    String descriptionImgUrl,
    long price,
    long quantity,
    Category category
) {


    static UpdateProductResponse of(UpdateProductServiceResponse serviceResponse) {
        return UpdateProductResponse.builder()
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
            .build();
    }
}

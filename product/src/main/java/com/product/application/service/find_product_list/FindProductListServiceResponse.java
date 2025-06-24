package com.product.application.service.find_product_list;

import com.product.domain.model.Product;
import lombok.Builder;

@Builder
public record FindProductListServiceResponse(
    Long id,
    String productName,
    String sellerEmail,
    String productImgUrl,
    long price
) {

    public static FindProductListServiceResponse of(Product product) {
        return FindProductListServiceResponse.builder()
            .id(product.getId())
            .productName(product.getProductName())
            .sellerEmail(product.getSellerEmail())
            .productImgUrl(product.getProductImgUrl())
            .price(product.getPrice())
            .build();
    }
}

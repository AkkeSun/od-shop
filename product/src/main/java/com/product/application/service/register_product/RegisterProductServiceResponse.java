package com.product.application.service.register_product;

import com.product.domain.model.Category;
import com.product.domain.model.Product;
import java.util.Set;
import lombok.Builder;

@Builder
public record RegisterProductServiceResponse(
    Long productId,
    String sellerEmail,
    String productName,
    String productImgUrl,
    String descriptionImgUrl,
    Set<String> keywords,
    Set<String> productOption,
    long price,
    long quantity,
    Category category
) {

    public static RegisterProductServiceResponse of(Product product) {
        return RegisterProductServiceResponse.builder()
            .productId(product.getId())
            .sellerEmail(product.getSellerEmail())
            .productName(product.getProductName())
            .productImgUrl(product.getProductImgUrl())
            .descriptionImgUrl(product.getDescriptionImgUrl())
            .keywords(product.getKeywords())
            .productOption(product.getProductOption())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .category(product.getCategory())
            .build();
    }
}

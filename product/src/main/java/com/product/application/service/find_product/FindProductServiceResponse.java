package com.product.application.service.find_product;

import com.product.domain.model.Category;
import com.product.domain.model.Product;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import lombok.Builder;

@Builder
public record FindProductServiceResponse(
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

    public static FindProductServiceResponse of(Product product) {
        return FindProductServiceResponse.builder()
            .productId(product.getId())
            .sellerEmail(product.getSellerEmail())
            .productName(product.getProductName())
            .productImgUrl(product.getProductImgUrl())
            .descriptionImgUrl(product.getDescriptionImgUrl())
            .keywords(product.getKeywords())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .category(product.getCategory())
            .regDateTime(
                product.getRegDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();
    }
}

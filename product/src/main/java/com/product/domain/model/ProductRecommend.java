package com.product.domain.model;

import lombok.Builder;

@Builder
public record ProductRecommend(
    Long id,
    Long productId,
    String productName,
    String sellerEmail,
    String productImgUrl,
    long price
) {

}

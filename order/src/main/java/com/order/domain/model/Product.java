package com.order.domain.model;

import lombok.Builder;

@Builder
public record Product(
    Long id,
    String sellerEmail,
    String productName,
    String productImgUrl,
    String descriptionImgUrl,
    long price,
    long buyQuantity,
    String category
) {
    
}

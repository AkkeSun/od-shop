package com.order.domain.model;

import java.util.Set;

public record Product(
    Long id,
    String sellerEmail,
    String productName,
    String productImgUrl,
    String descriptionImgUrl,
    Set<String> productOption,
    long price,
    long buyQuantity,
    String category
) {

}

package com.product.domain.model;

import lombok.Builder;

@Builder
public record ProductReserveHistory(
    Long id,
    Long productId,
    Long customerId,
    long reservedQuantity
) {

}

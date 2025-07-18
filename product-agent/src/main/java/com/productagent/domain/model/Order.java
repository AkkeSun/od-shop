package com.productagent.domain.model;

import lombok.Builder;

@Builder
public record Order(
    Long productId,
    long orderCount
) {

}

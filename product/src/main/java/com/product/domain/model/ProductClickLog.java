package com.product.domain.model;

import static com.product.infrastructure.util.DateUtil.getCurrentDateTime;

import lombok.Builder;

@Builder
public record ProductClickLog(
    Long productId,
    String regDateTime
) {

    public static ProductClickLog of(Long productId) {
        return ProductClickLog.builder()
            .productId(productId)
            .regDateTime(getCurrentDateTime())
            .build();
    }
}

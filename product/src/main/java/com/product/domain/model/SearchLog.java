package com.product.domain.model;

import static com.common.infrastructure.util.DateUtil.getCurrentDateTime;

import lombok.Builder;

@Builder
public record SearchLog(
    String query,
    String regDateTime
) {

    public static SearchLog of(String query) {
        return SearchLog.builder()
            .query(query)
            .regDateTime(getCurrentDateTime())
            .build();
    }
}

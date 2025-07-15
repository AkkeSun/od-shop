package com.product.domain.model;

import com.product.infrastructure.util.DateUtil;
import lombok.Builder;

@Builder
public record SearchLog(
    String query,
    String regDateTime
) {

    public static SearchLog of(String query) {
        return SearchLog.builder()
            .query(query)
            .regDateTime(DateUtil.getCurrentDateTime())
            .build();
    }
}

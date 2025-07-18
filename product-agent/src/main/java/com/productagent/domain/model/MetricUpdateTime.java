package com.productagent.domain.model;

import lombok.Builder;

@Builder
public record MetricUpdateTime(
    String regDateTime
) {

    public static MetricUpdateTime of(String regDateTime) {
        return MetricUpdateTime.builder()
            .regDateTime(regDateTime)
            .build();
    }
}

package com.productagent.domain.model;

import lombok.Builder;

@Builder
public record SearchLog(
    String query,
    String regDateTime
) {

}

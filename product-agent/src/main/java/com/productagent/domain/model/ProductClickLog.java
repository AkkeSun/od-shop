package com.productagent.domain.model;

import lombok.Builder;

@Builder
public record ProductClickLog(
    Long productId,
    String regDateTime
) {

}

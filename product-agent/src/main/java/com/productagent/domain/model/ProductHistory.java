package com.productagent.domain.model;

import lombok.Builder;

@Builder
public record ProductHistory(
    Long productId,
    Long accountId,
    String type,
    String detailInfo,
    String regDateTime
) {

}

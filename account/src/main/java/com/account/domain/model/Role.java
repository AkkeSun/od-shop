package com.account.domain.model;

import lombok.Builder;

@Builder
public record Role(
    Long id,
    String name,
    String description
) {

}

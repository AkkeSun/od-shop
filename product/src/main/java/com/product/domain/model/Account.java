package com.product.domain.model;

import io.jsonwebtoken.Claims;
import lombok.Builder;

@Builder
public record Account(
    Long id,
    String email,
    String role
) {

    public static Account of(Claims claims) {
        return Account.builder()
            .email(claims.getSubject())
            .id(Long.valueOf(claims.get("accountId").toString()))
            .role(claims.get("role").toString())
            .build();
    }
}

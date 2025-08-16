package com.order.domain.model;

import io.jsonwebtoken.Claims;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;

@Builder
public record Account(
    Long id,
    List<String> roles
) {

    public static Account of(Claims claims) {
        return Account.builder()
            .id(Long.valueOf(claims.get("accountId").toString()))
            .roles(Arrays.stream(claims.get("roles").toString().split(",")).toList())
            .build();
    }
}

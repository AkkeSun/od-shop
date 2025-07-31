package com.product.domain.model;

import lombok.Builder;

@Builder
public record AuthorizationRule(
    int sortOrder,
    String httpMethod,
    String uriPattern,
    String role
) {

    public boolean isAnonymous() {
        return role.equals("ANONYMOUS");
    }
}

package com.productagent.domain.model;

import lombok.Builder;

@Builder
public record DeleteAccountLog(
    Long accountId,
    String role
) {

    public boolean isSeller() {
        return role.equals("SELLER");
    }
}

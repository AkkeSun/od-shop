package com.account.domain.model;

import lombok.Builder;

@Builder
public record DeleteAccountLog(
    Long accountId,
    String role
) {

    public static DeleteAccountLog of(Account account) {
        return DeleteAccountLog.builder()
            .accountId(account.getId())
            .role(account.getRole().name())
            .build();
    }
}

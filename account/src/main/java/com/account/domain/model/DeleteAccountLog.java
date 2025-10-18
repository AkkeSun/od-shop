package com.account.domain.model;

import com.common.infrastructure.resolver.LoginAccountInfo;
import lombok.Builder;

@Builder
public record DeleteAccountLog(
    Long accountId,
    String roles
) {

    public static DeleteAccountLog of(LoginAccountInfo account) {
        return DeleteAccountLog.builder()
            .accountId(account.getId())
            .roles(String.join(",", account.getRoles()))
            .build();
    }
}

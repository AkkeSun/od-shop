package com.account.domain.model;

import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record DeleteAccountLog(
    Long accountId,
    String roles
) {

    public static DeleteAccountLog of(Account account) {
        return DeleteAccountLog.builder()
            .accountId(account.getId())
            .roles(account.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.joining(",")))
            .build();
    }
}

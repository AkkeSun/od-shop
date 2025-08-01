package com.account.domain.model;

import static com.account.infrastructure.util.DateUtil.getCurrentDateTime;

import lombok.Builder;

@Builder
public record LoginLog(
    Long accountId,
    String email,
    String loginDateTime
) {

    public static LoginLog of(Account account) {
        return LoginLog.builder()
            .accountId(account.getId())
            .email(account.getEmail())
            .loginDateTime(getCurrentDateTime())
            .build();
    }
}

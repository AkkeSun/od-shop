package com.account.domain.model;

import lombok.Builder;

@Builder
public record LoginLog(
    Long accountId,
    String email,
    String loginDateTime
) {

}

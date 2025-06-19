package com.account.applicaiton.port.in.command;

import lombok.Builder;

@Builder
public record UpdateAccountCommand(
    Long accountId,
    String password,
    String passwordCheck,
    String username,
    String userTel,
    String address
) {
}

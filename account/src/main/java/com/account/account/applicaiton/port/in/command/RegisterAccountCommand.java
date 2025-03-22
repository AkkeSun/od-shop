package com.account.account.applicaiton.port.in.command;

import lombok.Builder;

@Builder
public record RegisterAccountCommand(
    String email,
    String password,
    String role,
    String username,
    String userTel,
    String address
) {

}

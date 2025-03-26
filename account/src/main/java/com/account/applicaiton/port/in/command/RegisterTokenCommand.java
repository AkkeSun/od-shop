package com.account.applicaiton.port.in.command;

import lombok.Builder;

@Builder
public record RegisterTokenCommand(
    String email, String password
) {

}

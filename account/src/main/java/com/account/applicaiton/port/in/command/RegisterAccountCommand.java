package com.account.applicaiton.port.in.command;

import java.util.List;
import lombok.Builder;

@Builder
public record RegisterAccountCommand(
    String email,
    String password,
    List<String> roles,
    String username,
    String userTel,
    String address
) {

}

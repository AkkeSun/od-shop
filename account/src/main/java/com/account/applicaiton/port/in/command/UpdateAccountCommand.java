package com.account.applicaiton.port.in.command;

import lombok.Builder;
import org.springframework.util.StringUtils;

@Builder
public record UpdateAccountCommand(
    Long accountId,
    String password,
    String passwordCheck,
    String username,
    String userTel,
    String address
) {

    public boolean isUsernameUpdateRequired(String oldUsername) {
        return StringUtils.hasText(username) && !username.equals(oldUsername);
    }

    public boolean isPasswordUpdateRequired(String oldPassword) {
        return StringUtils.hasText(password) && !password.equals(oldPassword);
    }

    public boolean isUserTelUpdateRequired(String oldUserTel) {
        return StringUtils.hasText(userTel) && !userTel.equals(oldUserTel);
    }

    public boolean isAddressUpdateRequired(String oldAddress) {
        return StringUtils.hasText(address) && !address.equals(oldAddress);
    }
}

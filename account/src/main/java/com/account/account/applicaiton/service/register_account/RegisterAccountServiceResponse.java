package com.account.account.applicaiton.service.register_account;

import lombok.Builder;

@Builder
public record RegisterAccountServiceResponse(
    String accessToken, String refreshToken
) {

}

package com.account.applicaiton.service.update_token;

import lombok.Builder;

@Builder
public record UpdateTokenServiceResponse(
    String accessToken,
    String refreshToken
) {

}

package com.account.applicaiton.service.register_token_by_refresh;

import lombok.Builder;

@Builder
public record RegisterTokenByRefreshServiceResponse(
    String accessToken,
    String refreshToken
) {

}

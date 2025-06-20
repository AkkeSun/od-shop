package com.account.adapter.in.controller.register_token_by_refresh;

import com.account.applicaiton.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
class RegisterTokenByRefreshResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    RegisterTokenByRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    RegisterTokenByRefreshResponse of(RegisterTokenByRefreshServiceResponse serviceResponse) {
        return RegisterTokenByRefreshResponse.builder()
            .accessToken(serviceResponse.accessToken())
            .refreshToken(serviceResponse.refreshToken())
            .build();
    }

}

package com.account.adapter.in.controller.register_token;

import com.account.applicaiton.service.register_token.RegisterTokenServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
class RegisterTokenResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    RegisterTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    static RegisterTokenResponse of(RegisterTokenServiceResponse serviceResponse) {
        return RegisterTokenResponse.builder()
            .accessToken(serviceResponse.accessToken())
            .refreshToken(serviceResponse.refreshToken())
            .build();
    }

}

package com.account.adapter.in.controller.register_account;

import com.account.applicaiton.service.register_account.RegisterAccountServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
class RegisterAccountResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    RegisterAccountResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    RegisterAccountResponse of(RegisterAccountServiceResponse serviceResponse) {
        return RegisterAccountResponse.builder()
            .accessToken(serviceResponse.accessToken())
            .refreshToken(serviceResponse.refreshToken())
            .build();
    }
}

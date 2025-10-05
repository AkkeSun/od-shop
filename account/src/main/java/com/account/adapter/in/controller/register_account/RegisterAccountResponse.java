package com.account.adapter.in.controller.register_account;

import com.account.applicaiton.service.register_account.RegisterAccountServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
class RegisterAccountResponse {

    private String accessToken;
    private String refreshToken;

    static RegisterAccountResponse of(RegisterAccountServiceResponse serviceResponse) {
        return RegisterAccountResponse.builder()
            .accessToken(serviceResponse.accessToken())
            .refreshToken(serviceResponse.refreshToken())
            .build();
    }
}

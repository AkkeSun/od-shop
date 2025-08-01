package com.account.adapter.in.controller.update_token;

import com.account.applicaiton.service.update_token.UpdateTokenServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
class UpdateTokenResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    UpdateTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    static UpdateTokenResponse of(UpdateTokenServiceResponse serviceResponse) {
        return UpdateTokenResponse.builder()
            .accessToken(serviceResponse.accessToken())
            .refreshToken(serviceResponse.refreshToken())
            .build();
    }

}

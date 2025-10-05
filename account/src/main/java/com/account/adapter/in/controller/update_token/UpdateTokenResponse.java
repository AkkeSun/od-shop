package com.account.adapter.in.controller.update_token;

import com.account.applicaiton.service.update_token.UpdateTokenServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UpdateTokenResponse {

    private String accessToken;
    private String refreshToken;

    static UpdateTokenResponse of(UpdateTokenServiceResponse serviceResponse) {
        return UpdateTokenResponse.builder()
            .accessToken(serviceResponse.accessToken())
            .refreshToken(serviceResponse.refreshToken())
            .build();
    }

}

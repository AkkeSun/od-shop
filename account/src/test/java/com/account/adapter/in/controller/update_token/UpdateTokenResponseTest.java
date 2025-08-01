package com.account.adapter.in.controller.update_token;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.account.applicaiton.service.update_token.UpdateTokenServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateTokenResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 API 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체를 API 응답 객체로 잘 변환하는지 확인한다")
        void success() {
            // given
            UpdateTokenServiceResponse serviceResponse = UpdateTokenServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

            // when
            UpdateTokenResponse response =
                new UpdateTokenResponse().of(serviceResponse);

            // then
            assertEquals(response.getAccessToken(), serviceResponse.accessToken());
            assertEquals(response.getRefreshToken(), serviceResponse.refreshToken());
        }
    }
}
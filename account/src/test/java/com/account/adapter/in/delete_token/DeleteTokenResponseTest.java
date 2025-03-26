package com.account.adapter.in.delete_token;

import com.account.adapter.in.delete_token.DeleteTokenResponse;
import com.account.applicaiton.service.delete_token.DeleteTokenServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeleteTokenResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 API 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체를 API 응답 객체로 잘 변환하는지 확인한다")
        void success() {
            // given
            DeleteTokenServiceResponse serviceResponse = DeleteTokenServiceResponse.builder()
                .result("Y")
                .build();

            // when
            DeleteTokenResponse response = new DeleteTokenResponse().of(serviceResponse);

            // then
            assert response.getResult().equals(serviceResponse.result());
        }
    }
}
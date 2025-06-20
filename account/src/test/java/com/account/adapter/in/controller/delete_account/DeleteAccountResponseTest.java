package com.account.adapter.in.controller.delete_account;

import com.account.applicaiton.service.delete_account.DeleteAccountServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeleteAccountResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 API 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체를 API 응답 객체로 잘 변환하는지 확인한다")
        void success() {
            // given
            DeleteAccountServiceResponse serviceResponse = DeleteAccountServiceResponse.builder()
                .id(10L)
                .result("Y")
                .build();

            // when
            DeleteAccountResponse response = new DeleteAccountResponse().of(serviceResponse);

            // then
            assert response.getId().equals(serviceResponse.id());
            assert response.getResult().equals(serviceResponse.result());
        }
    }
}
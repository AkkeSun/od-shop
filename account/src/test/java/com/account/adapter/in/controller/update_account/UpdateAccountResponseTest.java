package com.account.adapter.in.controller.update_account;

import com.account.applicaiton.service.update_account.UpdateAccountServiceResponse;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateAccountResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 API 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체를 API 응답 객체로 잘 변환하는지 확인한다")
        void success() {
            // given
            UpdateAccountServiceResponse serviceResponse = UpdateAccountServiceResponse.builder()
                .updateYn("Y")
                .updateList(Arrays.asList("username", "userTel"))
                .build();

            // when
            UpdateAccountResponse response = new UpdateAccountResponse().of(serviceResponse);

            // then
            assert response.getUpdateYn().equals(serviceResponse.updateYn());
            assert response.getUpdateList().equals(serviceResponse.updateList());
        }
    }
}
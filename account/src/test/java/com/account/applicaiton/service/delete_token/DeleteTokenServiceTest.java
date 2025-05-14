package com.account.applicaiton.service.delete_token;

import com.account.fakeClass.FakeCachePortClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeTokenStoragePortClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class DeleteTokenServiceTest {

    DeleteTokenService service;

    @BeforeEach
    void setup() {
        service = new DeleteTokenService(
            new FakeJwtUtilClass(),
            new FakeCachePortClass(),
            new FakeTokenStoragePortClass()
        );
    }

    @Nested
    @DisplayName("[deleteToken] 토큰을 삭제하는 메소드")
    class Describe_deleteToken {

        @Test
        @DisplayName("[success] 유효한 인증 토큰 이라면 토큰을 삭제하는지 확인한다.")
        void success(CapturedOutput output) {
            // given
            String authentication = "valid token";

            // when
            DeleteTokenServiceResponse result = service.deleteToken(authentication);

            // then
            assert result.result().equals("Y");
            assert output.toString().contains("FakeTokenStoragePortClass deleteByEmail");
            assert output.toString().contains("FakeCachePortClass deleteTokenByEmail");
        }
    }
}
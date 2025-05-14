package com.account.applicaiton.service.register_token;

import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeCachePortClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeMessageProducerPortClass;
import com.account.fakeClass.FakeTokenStoragePortClass;
import com.account.fakeClass.FakeUserAgentUtilClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class RegisterTokenServiceTest {

    RegisterTokenService service;

    @BeforeEach
    void setup() {
        service = new RegisterTokenService(
            new FakeJwtUtilClass(),
            new FakeCachePortClass(),
            new FakeUserAgentUtilClass(),
            new FakeTokenStoragePortClass(),
            new FakeAccountStorageClass(),
            new FakeMessageProducerPortClass()
        );
    }

    @Nested
    @DisplayName("[registerToken] 토큰을 등록하는 메소드")
    class Describe_registerToken {
        
        @Test
        @DisplayName("[success] 토큰을 발급 및 저장하고 로그인 로그 메시지를 발송한 후 성공 메시지를 응답한다.")
        void success(CapturedOutput output) {
            RegisterTokenCommand command = RegisterTokenCommand.builder()
                .email("success")
                .password("password")
                .build();

            // when
            RegisterTokenServiceResponse response = service.registerToken(command);

            // then
            assert response.accessToken().equals("valid token");
            assert response.refreshToken().equals("valid refresh token");
            assert output.toString().contains("FakeCachePortClass registerToken");
            assert output.toString().contains("FakeTokenStoragePortClass registerToken");
            assert output.toString().contains("[account-login] ==>");
        }
    }
}
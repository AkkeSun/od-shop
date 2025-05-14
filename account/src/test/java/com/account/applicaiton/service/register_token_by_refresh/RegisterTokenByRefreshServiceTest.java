package com.account.applicaiton.service.register_token_by_refresh;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.fakeClass.FakeCachePortClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeTokenStoragePortClass;
import com.account.fakeClass.FakeUserAgentUtilClass;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class RegisterTokenByRefreshServiceTest {

    RegisterTokenByRefreshService service;

    @BeforeEach
    void setup() {
        service = new RegisterTokenByRefreshService(
            new FakeJwtUtilClass(),
            new FakeCachePortClass(),
            new FakeUserAgentUtilClass(),
            new FakeTokenStoragePortClass()
        );
    }

    @Nested
    @DisplayName("[registerTokenByRefresh] 리프레시 토큰으로 인증 토큰을 갱신하는 메소드")
    class Describe_registerTokenByRefresh {

        @Test
        @DisplayName("[success] 리프레시 토큰이 캐시에 저장되어 있고 유효 하다면 인증 토큰을 갱신한 후 응답한다.")
        void success1(CapturedOutput output) {
            // given
            String refreshToken = "valid refresh token";

            // when
            RegisterTokenByRefreshServiceResponse serviceResponse = service
                .registerTokenByRefresh(refreshToken);

            // then
            assert serviceResponse.accessToken().equals("valid token");
            assert serviceResponse.refreshToken().equals("valid refresh token");
            assert output.toString().contains("FakeCachePortClass registerToken");
            assert output.toString().contains("FakeTokenStoragePortClass updateToken");
        }


        @Test
        @DisplayName("[success] 리프레시 토큰이 캐시에 저장되어 있지 않고 db 에만 저장되어 있고 유효 하다면 인증 토큰을 갱신한 후 응답한다.")
        void success2(CapturedOutput output) {
            // given
            String refreshToken = "valid refresh token2";

            // when
            RegisterTokenByRefreshServiceResponse serviceResponse = service
                .registerTokenByRefresh(refreshToken);

            // then
            assert serviceResponse.accessToken().equals("valid token");
            assert serviceResponse.refreshToken().equals("valid refresh token");
            assert output.toString().contains("FakeCachePortClass registerToken");
            assert output.toString().contains("FakeTokenStoragePortClass updateToken");
            assert output.toString().contains("[token cache notfound]");

        }

        @Test
        @DisplayName("[error] 리프레시 토큰이 저장된 토큰과 다르다면 CustomAuthenticationException 을 응답한다.")
        void error4(CapturedOutput output) {
            // given
            String refreshToken = "valid refresh token3";

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () ->
                    service.registerTokenByRefresh(refreshToken));

            // then
            assert output.toString().contains("[invalid refresh token]");
            assert exception.getErrorCode().equals(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("[error] 리프레시 토큰이 레디스와 DB에 저장되어 있지 않다면 CustomAuthenticationException 을 응답한다.")
        void error3(CapturedOutput output) {
            // given
            String refreshToken = "valid refresh token4";

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class,
                () -> service.registerTokenByRefresh(refreshToken));

            // then
            assert output.toString().contains("[invalid refresh token]");
            assert exception.getErrorCode().equals(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
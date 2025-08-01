package com.account.applicaiton.service.update_token;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.domain.model.Account;
import com.account.domain.model.RefreshTokenInfo;
import com.account.domain.model.Role;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeRedisStoragePortClass;
import com.account.fakeClass.FakeTokenStoragePortClass;
import com.account.fakeClass.StubUserAgentUtilClass;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.ErrorCode;
import com.account.infrastructure.util.JsonUtil;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(OutputCaptureExtension.class)
class UpdateTokenByRefreshServiceTest {

    UpdateTokenService service;
    FakeJwtUtilClass fakeJwtUtilClass;
    FakeRedisStoragePortClass fakeRedisStoragePortClass;
    StubUserAgentUtilClass fakeUserAgentUtilClass;
    FakeTokenStoragePortClass fakeTokenStoragePortClass;
    FakeAccountStorageClass fakeAccountStorageClass;

    UpdateTokenByRefreshServiceTest() {
        fakeJwtUtilClass = new FakeJwtUtilClass();
        fakeRedisStoragePortClass = new FakeRedisStoragePortClass();
        fakeUserAgentUtilClass = new StubUserAgentUtilClass();
        fakeTokenStoragePortClass = new FakeTokenStoragePortClass();
        fakeAccountStorageClass = new FakeAccountStorageClass();

        service = new UpdateTokenService(
            fakeJwtUtilClass,
            fakeUserAgentUtilClass,
            fakeRedisStoragePortClass,
            fakeTokenStoragePortClass
        );

        ReflectionTestUtils
            .setField(service, "tokenRedisKey", "token::%s-%s");
        ReflectionTestUtils
            .setField(service, "refreshTokenTtl", 99999999L);
    }

    @BeforeEach
    void setup() {
        Account account = Account.builder()
            .id(1L)
            .email("od@test.com")
            .username("od")
            .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
            .regDate("20240101")
            .userTel("01012341234")
            .roles(List.of(Role.builder().id(1L).name("ROLE_CUSTOMER").build()))
            .password("1234")
            .build();
        fakeAccountStorageClass.register(account);
        fakeRedisStoragePortClass.redisData.clear();
        fakeTokenStoragePortClass.tokenList.clear();
    }

    @Nested
    @DisplayName("[registerTokenByRefresh] 리프레시 토큰으로 인증 토큰을 갱신하는 메소드")
    class Describe_registerTokenByRefresh {

        @Test
        @DisplayName("[success] 리프레시 토큰이 캐시에 저장되어 있고 유효 하다면 인증 토큰을 갱신한 후 응답한다.")
        void success1(CapturedOutput output) {
            // given
            String refreshToken = "valid refresh token - od@test.com";
            RefreshTokenInfo token = RefreshTokenInfo.builder()
                .accountId(1L)
                .email("od@test.com")
                .userAgent(fakeUserAgentUtilClass.getUserAgent())
                .refreshToken(refreshToken)
                .roles("ROLE_CUSTOMER")
                .build();
            String key = String.format("token::%s-%s", token.getEmail(), token.getUserAgent());
            fakeRedisStoragePortClass.register(key, JsonUtil.toJsonString(token), 10L);

            // when
            UpdateTokenServiceResponse serviceResponse = service.update(refreshToken);

            // then
            assert serviceResponse.accessToken().equals("valid token - od@test.com");
            assert serviceResponse.refreshToken().equals("valid refresh token - od@test.com");
            assert output.toString().contains("FakeCachePortClass registerToken");
            assert output.toString().contains("FakeTokenStoragePortClass updateToken");
        }


        @Test
        @DisplayName("[success] 리프레시 토큰이 캐시에 저장되어 있지 않고 db 에만 저장되어 있고 유효 하다면 인증 토큰을 갱신한 후 응답한다.")
        void success2(CapturedOutput output) {
            // given
            String refreshToken = "valid refresh token - od@test.com";
            RefreshTokenInfo token = RefreshTokenInfo.builder()
                .accountId(1L)
                .email("od@test.com")
                .userAgent(fakeUserAgentUtilClass.getUserAgent())
                .refreshToken(refreshToken)
                .roles("ROLE_CUSTOMER")
                .build();
            fakeTokenStoragePortClass.registerToken(token);

            // when
            UpdateTokenServiceResponse serviceResponse = service.update(refreshToken);

            // then
            assert serviceResponse.accessToken().equals("valid token - od@test.com");
            assert serviceResponse.refreshToken().equals("valid refresh token - od@test.com");
            assert output.toString().contains("FakeCachePortClass registerToken");
            assert output.toString().contains("FakeTokenStoragePortClass updateToken");
            assert output.toString().contains("[token cache notfound]");

        }

        @Test
        @DisplayName("[error] 리프레시 토큰이 저장된 토큰과 다르다면 CustomAuthenticationException 을 응답한다.")
        void error4(CapturedOutput output) {
            // given
            String refreshToken = "valid refresh token - od@test.com";
            RefreshTokenInfo token = RefreshTokenInfo.builder()
                .accountId(1L)
                .email("od@test.com")
                .userAgent(fakeUserAgentUtilClass.getUserAgent())
                .refreshToken("test")
                .roles("ROLE_CUSTOMER")
                .build();
            String key = String.format("token::%s-%s", token.getEmail(), token.getUserAgent());
            fakeRedisStoragePortClass.register(key, JsonUtil.toJsonString(token), 10L);

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () -> service.update(refreshToken));

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
                () -> service.update(refreshToken));

            // then
            assert output.toString().contains("[invalid refresh token]");
            assert exception.getErrorCode().equals(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
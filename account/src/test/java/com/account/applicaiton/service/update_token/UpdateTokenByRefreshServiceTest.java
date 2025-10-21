package com.account.applicaiton.service.update_token;

import static com.common.infrastructure.util.JsonUtil.toJsonString;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.domain.model.Account;
import com.account.domain.model.RefreshTokenInfo;
import com.account.domain.model.Role;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeRedisStoragePortClass;
import com.account.fakeClass.StubUserAgentUtilClass;
import com.account.fakeClass.TestPropertiesHelper;
import com.common.infrastructure.exception.CustomAuthenticationException;
import com.common.infrastructure.exception.ErrorCode;
import com.common.infrastructure.util.JwtUtil;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class UpdateTokenByRefreshServiceTest {

    UpdateTokenService service;
    FakeRedisStoragePortClass fakeRedisStoragePortClass;
    StubUserAgentUtilClass fakeUserAgentUtilClass;
    FakeAccountStorageClass fakeAccountStorageClass;

    UpdateTokenByRefreshServiceTest() {
        fakeRedisStoragePortClass = new FakeRedisStoragePortClass();
        fakeUserAgentUtilClass = new StubUserAgentUtilClass();
        fakeAccountStorageClass = new FakeAccountStorageClass();

        service = new UpdateTokenService(
            TestPropertiesHelper.createRedisProperties(),
            fakeUserAgentUtilClass,
            fakeRedisStoragePortClass
        );
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
    }

    @Nested
    @DisplayName("[registerTokenByRefresh] 리프레시 토큰으로 인증 토큰을 갱신하는 메소드")
    class Describe_registerTokenByRefresh {

        @Test
        @DisplayName("[success] 리프레시 토큰이 캐시에 저장되어 있고 유효 하다면 인증 토큰을 갱신한 후 응답한다.")
        void success1(CapturedOutput output) {
            // given
            String refreshToken = JwtUtil.createRefreshToken("od@test.com");
            RefreshTokenInfo token = RefreshTokenInfo.builder()
                .accountId(1L)
                .email("od@test.com")
                .userAgent(fakeUserAgentUtilClass.getUserAgent())
                .refreshToken(refreshToken)
                .roles(List.of("ROLE_CUSTOMER"))
                .build();
            String key = String.format("token::%s-%s", token.getEmail(), token.getUserAgent());
            fakeRedisStoragePortClass.register(key, toJsonString(token), 10L);

            // when
            UpdateTokenServiceResponse serviceResponse = service.update(refreshToken);

            // then
            assert JwtUtil.validateTokenExceptExpiration(serviceResponse.accessToken());
            assert JwtUtil.getEmail(serviceResponse.accessToken()).equals(token.getEmail());
            assert JwtUtil.getAccountId(serviceResponse.accessToken()).equals(token.getAccountId());
            assert output.toString().contains("FakeCachePortClass registerToken");
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
                .roles(List.of("ROLE_CUSTOMER"))
                .build();
            String key = String.format("token::%s-%s", token.getEmail(), token.getUserAgent());
            fakeRedisStoragePortClass.register(key, toJsonString(token), 10L);

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () -> service.update(refreshToken));

            // then
            assert exception.getErrorCode().equals(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("[error] 리프레시 토큰이 저장되어 있지 않다면 CustomAuthenticationException 을 응답한다.")
        void error3(CapturedOutput output) {
            // given
            String refreshToken = "error";

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class,
                () -> service.update(refreshToken));

            // then
            assert exception.getErrorCode().equals(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
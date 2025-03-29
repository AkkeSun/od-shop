package com.account.applicaiton.service.register_token_by_refresh;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.account.IntegrationTestSupport;
import com.account.applicaiton.port.out.CachePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Token;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.ErrorCode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;

class RegisterTokenByRefreshServiceTest extends IntegrationTestSupport {

    @Autowired
    RegisterTokenByRefreshService service;

    @Autowired
    TokenStoragePort tokenStoragePort;

    @Autowired
    CachePort cachePort;

    @Nested
    @DisplayName("[registerTokenByRefresh] 리프레시 토큰으로 인증 토큰을 갱신하는 메소드")
    class Describe_registerTokenByRefresh {

        @Test
        @DisplayName("[success] 리프레시 토큰이 캐시에 저장되어 있고 유효 하다면 인증 토큰을 갱신한 후 응답한다.")
        void success1() {
            // given
            Token token = Token.builder()
                .email("od@registerTokenByRefresh.success")
                .refreshToken(jwtUtil.createRefreshToken("od@registerTokenByRefresh.success"))
                .userAgent("chrome")
                .regDateTime(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .role("ROLE_CUSTOMER")
                .build();
            tokenStoragePort.registerToken(token);
            cachePort.registerToken(token);
            given(userAgentUtil.getUserAgent()).willReturn("chrome");

            // when
            RegisterTokenByRefreshServiceResponse serviceResponse = service
                .registerTokenByRefresh(token.getRefreshToken());
            Token redisToken = cachePort
                .findTokenByEmailAndUserAgent(token.getEmail(), token.getUserAgent());
            Token dbToken = tokenStoragePort
                .findByEmailAndUserAgent(token.getEmail(), token.getUserAgent());

            // then
            assert redisToken.getRefreshToken().equals(serviceResponse.refreshToken());
            assert redisToken.getEmail().equals(token.getEmail());
            assert redisToken.getUserAgent().equals(token.getUserAgent());
            assert redisToken.getRole().equals(token.getRole());
            assert dbToken.getRefreshToken().equals(serviceResponse.refreshToken());
            assert dbToken.getEmail().equals(token.getEmail());
            assert dbToken.getUserAgent().equals(token.getUserAgent());
            assert dbToken.getRole().equals(token.getRole());

            // cleanup
            tokenStoragePort.deleteByEmail(token.getEmail());
            redisTemplate.delete("token::" + token.getEmail() + "-" + token.getUserAgent());
        }

        @Test
        @DisplayName("[success] 리프레시 토큰이 캐시에 저장되어 있지 않고 db 에만 저장되어 있고 유효 하다면 인증 토큰을 갱신한 후 응답한다.")
        void success2(CapturedOutput output) {
            // given
            Token token = Token.builder()
                .email("od@registerTokenByRefresh.success")
                .refreshToken(jwtUtil.createRefreshToken("od@registerTokenByRefresh.success"))
                .userAgent("chrome")
                .regDateTime(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .role("ROLE_CUSTOMER")
                .build();
            tokenStoragePort.registerToken(token);
            given(userAgentUtil.getUserAgent()).willReturn("chrome");

            // when
            RegisterTokenByRefreshServiceResponse serviceResponse = service
                .registerTokenByRefresh(token.getRefreshToken());
            Token redisToken = cachePort
                .findTokenByEmailAndUserAgent(token.getEmail(), token.getUserAgent());
            Token dbToken = tokenStoragePort
                .findByEmailAndUserAgent(token.getEmail(), token.getUserAgent());

            // then
            assert output.toString().contains("[token cache notfound]");
            assert redisToken.getRefreshToken().equals(serviceResponse.refreshToken());
            assert redisToken.getEmail().equals(token.getEmail());
            assert redisToken.getUserAgent().equals(token.getUserAgent());
            assert redisToken.getRole().equals(token.getRole());
            assert dbToken.getRefreshToken().equals(serviceResponse.refreshToken());
            assert dbToken.getEmail().equals(token.getEmail());
            assert dbToken.getUserAgent().equals(token.getUserAgent());
            assert dbToken.getRole().equals(token.getRole());

            // cleanup
            tokenStoragePort.deleteByEmail(token.getEmail());
        }

        @Test
        @DisplayName("[error] 리프레시 토큰이 유효하지 않다면 CustomAuthenticationException 을 응답한다.")
        void error1(CapturedOutput output) {
            // given
            circuitBreakerRegistry.circuitBreaker("redis").reset();
            String refreshToken = "invalid-refresh-token";

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () ->
                    service.registerTokenByRefresh(refreshToken));

            // then
            assert !output.toString().contains("[token cache notfound]");
            assert !output.toString().contains("[invalid refresh token]");
            assert exception.getErrorCode().equals(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("[error] 리프레시 토큰의 유효기간이 만료 되었다면 CustomAuthenticationException 을 응답한다.")
        void error2(CapturedOutput output) throws InterruptedException {
            // given
            String refreshToken = jwtUtil.createRefreshToken("helloJwt");
            Thread.sleep(12000);

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () ->
                    service.registerTokenByRefresh(refreshToken));

            // then
            assert !output.toString().contains("[token cache notfound]");
            assert !output.toString().contains("[invalid refresh token]");
            assert exception.getErrorCode().equals(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("[error] 리프레시 토큰이 레디스와 DB에 저장되어 있지 않다면 CustomAuthenticationException 을 응답한다.")
        void error3(CapturedOutput output) {
            // given
            String refreshToken = jwtUtil.createRefreshToken("helloJwt");

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class,
                () -> service.registerTokenByRefresh(refreshToken));

            // then
            assert output.toString().contains("[invalid refresh token]");
            assert exception.getErrorCode().equals(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("[error] 리프레시 토큰이 저장된 토큰과 다르다면 CustomAuthenticationException 을 응답한다.")
        void error4(CapturedOutput output) {
            // given
            circuitBreakerRegistry.circuitBreaker("redis").reset();
            Token token = Token.builder()
                .email("helloJwt")
                .refreshToken(jwtUtil.createRefreshToken("anotherToken"))
                .userAgent("chrome")
                .regDateTime(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .role("ROLE_CUSTOMER")
                .build();
            tokenStoragePort.registerToken(token);
            given(userAgentUtil.getUserAgent()).willReturn("chrome");
            String refreshToken = jwtUtil.createRefreshToken("helloJwt");

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () ->
                    service.registerTokenByRefresh(refreshToken));

            // then
            assert output.toString().contains("[invalid refresh token]");
            assert exception.getErrorCode().equals(ErrorCode.INVALID_REFRESH_TOKEN);

            // cleanup
            tokenStoragePort.deleteByEmail(token.getEmail());
        }
    }
}
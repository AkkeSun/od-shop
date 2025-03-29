package com.account.applicaiton.service.delete_token;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.IntegrationTestSupport;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.CachePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.domain.model.Token;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.ErrorCode;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DeleteTokenServiceTest extends IntegrationTestSupport {

    @Autowired
    DeleteTokenService service;
    @Autowired
    AccountStoragePort accountStoragePort;
    @Autowired
    TokenStoragePort tokenStoragePort;
    @Autowired
    CachePort cachePort;

    @Nested
    @DisplayName("[deleteToken] 토큰을 삭제하는 메소드")
    class Describe_deleteToken {

        @Test
        @DisplayName("[success] 유효한 인증 토큰 이라면 토큰을 삭제하는지 확인한다.")
        void success() throws InterruptedException {
            // given
            Account account = Account.builder()
                .email("od@test.com")
                .username("od")
                .regDateTime(LocalDateTime.now())
                .regDate("20240101")
                .userTel("01012341234")
                .role(Role.ROLE_CUSTOMER)
                .password(aesUtil.encryptText("1234"))
                .build();
            Account savedAccount = accountStoragePort.register(account);
            Token token = Token.builder()
                .email(account.getEmail())
                .accountId(savedAccount.getId())
                .email(account.getEmail())
                .userAgent("userAgent")
                .refreshToken("refreshToken")
                .regDateTime("regDateTime")
                .role("role")
                .build();
            tokenStoragePort.registerToken(token);
            cachePort.registerToken(token);
            Thread.sleep(500);
            String authentication = jwtUtil.createAccessToken(savedAccount);

            // when
            DeleteTokenServiceResponse result = service.deleteToken(authentication);
            Thread.sleep(500);

            Token savedToken = tokenStoragePort.findByEmailAndUserAgent(
                token.getEmail(), token.getUserAgent());
            Token savedTokenCache = cachePort.findTokenByEmailAndUserAgent(
                token.getEmail(), token.getUserAgent());

            // then
            assert result.result().equals("Y");
            assert savedToken == null;
            assert savedTokenCache == null;

            // clean up
            accountStoragePort.deleteById(savedAccount.getId());
        }

        @Test
        @DisplayName("[error] 유효하지 않은 인증토큰 이라면 예외를 응답하는지 확인한다.")
        void error() {
            // given
            String accessToken = "error";

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () -> service.deleteToken(accessToken));

            // then
            assert exception.getErrorCode() == ErrorCode.INVALID_ACCESS_TOKEN;
        }
    }
}
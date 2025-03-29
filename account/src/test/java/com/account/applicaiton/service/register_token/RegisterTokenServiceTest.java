package com.account.applicaiton.service.register_token;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.account.IntegrationTestSupport;
import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.CachePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.domain.model.Token;
import com.account.infrastructure.exception.CustomNotFoundException;
import com.account.infrastructure.exception.ErrorCode;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;

class RegisterTokenServiceTest extends IntegrationTestSupport {

    @Autowired
    RegisterTokenService service;
    @Autowired
    AccountStoragePort accountStoragePort;
    @Autowired
    CachePort cachePort;
    @Autowired
    TokenStoragePort tokenStoragePort;

    @Nested
    @DisplayName("[registerToken] 토큰을 등록하는 메소드")
    class Describe_registerToken {

        @Test
        @DisplayName("[error] 등록된 사용자 정보가 아니라면 예외를 응답한다.")
        void error() {
            // given
            RegisterTokenCommand command = RegisterTokenCommand.builder()
                .email("error")
                .password("error")
                .build();

            // when
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> service.registerToken(command));

            // then
            assert exception.getErrorCode().equals(ErrorCode.DoesNotExist_ACCOUNT_INFO);
        }

        @Test
        @DisplayName("[success] 등록된 사용자 정보라면 토큰을 발급 및 저장하고 로그인 로그 메시지를 발송한 후 성공 메시지를 응답한다.")
        void success(CapturedOutput output) throws InterruptedException {
            Account account = Account.builder()
                .email("od@test.com")
                .username("od")
                .regDateTime(LocalDateTime.now())
                .regDate("20240101")
                .userTel("01012341234")
                .role(Role.ROLE_CUSTOMER)
                .password("1234")
                .build();
            given(userAgentUtil.getUserAgent()).willReturn("chrome");
            Account savedAccount = accountStoragePort.register(account);
            RegisterTokenCommand command = RegisterTokenCommand.builder()
                .email(account.getEmail())
                .password(account.getPassword())
                .build();

            // when
            RegisterTokenServiceResponse response = service.registerToken(command);
            Token savedToken = tokenStoragePort.findByEmailAndUserAgent(
                account.getEmail(), "chrome");
            Token savedTokenCache = cachePort.findTokenByEmailAndUserAgent(
                account.getEmail(), "chrome");
            Thread.sleep(1000);

            // then
            assert !response.accessToken().isEmpty();
            assert !response.refreshToken().isEmpty();
            assert savedToken.getEmail().equals(account.getEmail());
            assert savedToken.getUserAgent().equals("chrome");
            assert savedToken.getRole().equals(account.getRole().name());
            assert savedTokenCache.getEmail().equals(account.getEmail());
            assert savedTokenCache.getUserAgent().equals("chrome");
            assert savedTokenCache.getRole().equals(account.getRole().name());
            assert output.toString().contains("[account-login] ==>");

            // clean up
            accountStoragePort.deleteById(savedAccount.getId());
            tokenStoragePort.deleteByEmail(account.getEmail());
            cachePort.deleteTokenByEmail(account.getEmail());
        }
    }
}
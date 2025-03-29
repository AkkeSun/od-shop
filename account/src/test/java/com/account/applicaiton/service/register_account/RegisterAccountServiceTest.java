package com.account.applicaiton.service.register_account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.account.IntegrationTestSupport;
import com.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.CachePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.Token;
import com.account.infrastructure.exception.CustomBusinessException;
import com.account.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegisterAccountServiceTest extends IntegrationTestSupport {

    @Autowired
    RegisterAccountService service;
    @Autowired
    AccountStoragePort accountStoragePort;
    @Autowired
    TokenStoragePort tokenStoragePort;
    @Autowired
    CachePort cachePort;

    @Nested
    @DisplayName("[registerAccount] 사용자 정보를 등록하는 메소드")
    class registerAccount {

        @Test
        @DisplayName("[success] 등록된 사용자 정보가 아니라면 사용자 정보와 토큰을 등록하고 토큰을 응답한다.")
        void success() {
            // given
            RegisterAccountCommand command = RegisterAccountCommand.builder()
                .email("aaa.success")
                .address("registerAccount.success")
                .password("registerAccount.success")
                .userTel("01012341234")
                .role("ROLE_CUSTOMER")
                .build();
            given(userAgentUtil.getUserAgent()).willReturn("chrome");

            // when
            RegisterAccountServiceResponse response = service.registerAccount(command);

            Account account = accountStoragePort.findByEmailAndPassword(command.email(),
                command.password());
            Token dbToken = tokenStoragePort.findByEmailAndUserAgent(command.email(), "chrome");
            Token cacheToken = cachePort.findTokenByEmailAndUserAgent(command.email(), "chrome");

            // then
            assertThat(account.getEmail()).isEqualTo(command.email());
            assertThat(account.getAddress()).isEqualTo(command.address());
            assertThat(account.getPassword()).isEqualTo(command.password());
            assertThat(account.getUserTel()).isEqualTo(command.userTel());
            assertThat(account.getRole().name()).isEqualTo(command.role());
            assertThat(cacheToken.getRefreshToken()).isEqualTo(response.refreshToken());
            assertThat(cacheToken.getEmail()).isEqualTo(command.email());
            assertThat(cacheToken.getUserAgent()).isEqualTo("chrome");
            assertThat(cacheToken.getRole()).isEqualTo(command.role());
            assertThat(cacheToken.getRegDateTime()).isEqualTo(dbToken.getRegDateTime());
            assertThat(dbToken.getRefreshToken()).isEqualTo(response.refreshToken());
            assertThat(dbToken.getEmail()).isEqualTo(command.email());
            assertThat(dbToken.getUserAgent()).isEqualTo("chrome");
            assertThat(dbToken.getRole()).isEqualTo(command.role());

            // clean up
            accountStoragePort.deleteById(jwtUtil.getAccountId(response.accessToken()));
            tokenStoragePort.deleteByEmail(command.email());
            cachePort.deleteTokenByEmail(command.email());
            redisTemplate.delete(command.email() + "-chrome::token");
        }

        @Test
        @DisplayName("[error] 등록된 사용자 정보라면 CustomBusinessException 을 응답한다.")
        void error() {
            // given
            RegisterAccountCommand command = RegisterAccountCommand.builder()
                .email("registerAccount.error")
                .address("registerAccount.error")
                .password("registerAccount.error")
                .userTel("01012341234")
                .role("ROLE_CUSTOMER")
                .build();
            given(userAgentUtil.getUserAgent()).willReturn("chrome");
            RegisterAccountServiceResponse response = service.registerAccount(command);

            // when
            CustomBusinessException exception = assertThrows(CustomBusinessException.class,
                () -> service.registerAccount(command));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.Business_SAVED_ACCOUNT_INFO);
            accountStoragePort.deleteById(jwtUtil.getAccountId(response.accessToken()));
            tokenStoragePort.deleteByEmail(command.email());
        }
    }
}
package com.account.applicaiton.service.register_account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeCachePortClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeTokenStoragePortClass;
import com.account.fakeClass.FakeUserAgentUtilClass;
import com.account.infrastructure.exception.CustomBusinessException;
import com.account.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class RegisterAccountServiceTest {

    RegisterAccountService service;

    @BeforeEach
    void setup() {
        service = new RegisterAccountService(
            new FakeJwtUtilClass(),
            new FakeCachePortClass(),
            new FakeUserAgentUtilClass(),
            new FakeTokenStoragePortClass(),
            new FakeAccountStorageClass()
        );
    }

    @Nested
    @DisplayName("[registerAccount] 사용자 정보를 등록하는 메소드")
    class registerAccount {

        @Test
        @DisplayName("[success] 등록된 사용자 정보가 아니라면 사용자 정보와 토큰을 등록하고 토큰을 응답한다.")
        void success(CapturedOutput output) {
            // given
            RegisterAccountCommand command = RegisterAccountCommand.builder()
                .email("new email")
                .address("registerAccount.success")
                .password("registerAccount.success")
                .userTel("01012341234")
                .role("ROLE_CUSTOMER")
                .build();

            // when
            RegisterAccountServiceResponse response = service.registerAccount(command);

            // then
            assert response.accessToken().equals("valid token");
            assert response.refreshToken().equals("valid refresh token");
            assert output.toString().contains("FakeCachePortClass registerToken");
            assert output.toString().contains("FakeTokenStoragePortClass registerToken");
        }

        @Test
        @DisplayName("[error] 등록된 사용자 정보라면 CustomBusinessException 을 응답한다.")
        void error() {
            // given
            RegisterAccountCommand command = RegisterAccountCommand.builder()
                .email("success")
                .address("registerAccount.error")
                .password("registerAccount.error")
                .userTel("01012341234")
                .role("ROLE_CUSTOMER")
                .build();

            // when
            CustomBusinessException exception = assertThrows(CustomBusinessException.class,
                () -> service.registerAccount(command));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.Business_SAVED_ACCOUNT_INFO);
        }
    }
}
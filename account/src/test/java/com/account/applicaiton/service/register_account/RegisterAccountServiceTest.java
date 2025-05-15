package com.account.applicaiton.service.register_account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeCachePortClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeTokenStoragePortClass;
import com.account.fakeClass.StubUserAgentUtilClass;
import com.account.infrastructure.exception.CustomBusinessException;
import com.account.infrastructure.exception.ErrorCode;
import java.time.LocalDateTime;
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
    FakeJwtUtilClass fakeJwtUtilClass;
    FakeCachePortClass fakeCachePortClass;
    StubUserAgentUtilClass stubUserAgentUtilClass;
    FakeTokenStoragePortClass fakeTokenStoragePortClass;
    FakeAccountStorageClass fakeAccountStorageClass;

    RegisterAccountServiceTest() {
        fakeJwtUtilClass = new FakeJwtUtilClass();
        fakeCachePortClass = new FakeCachePortClass();
        stubUserAgentUtilClass = new StubUserAgentUtilClass();
        fakeTokenStoragePortClass = new FakeTokenStoragePortClass();
        fakeAccountStorageClass = new FakeAccountStorageClass();

        service = new RegisterAccountService(
            fakeJwtUtilClass,
            fakeCachePortClass,
            stubUserAgentUtilClass,
            fakeTokenStoragePortClass,
            fakeAccountStorageClass
        );
    }

    @BeforeEach
    void setup() {
        fakeAccountStorageClass.accountList.clear();
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
            assert response.accessToken().equals("valid token - new email");
            assert response.refreshToken().equals("valid refresh token - new email");
            assert output.toString().contains("FakeCachePortClass registerToken");
            assert output.toString().contains("FakeTokenStoragePortClass registerToken");
        }

        @Test
        @DisplayName("[error] 등록된 사용자 정보라면 CustomBusinessException 을 응답한다.")
        void error() {
            // given
            Account account = Account.builder()
                .id(1L)
                .email("od@test.com")
                .username("od")
                .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .regDate("20240101")
                .userTel("01012341234")
                .role(Role.ROLE_CUSTOMER)
                .password("1234")
                .build();
            fakeAccountStorageClass.register(account);
            // given
            RegisterAccountCommand command = RegisterAccountCommand.builder()
                .email(account.getEmail())
                .build();

            // when
            CustomBusinessException exception = assertThrows(CustomBusinessException.class,
                () -> service.registerAccount(command));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.Business_SAVED_ACCOUNT_INFO);
        }
    }
}
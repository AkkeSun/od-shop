package com.account.applicaiton.service.register_account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeRedisStoragePortClass;
import com.account.fakeClass.FakeRoleStoragePort;
import com.account.fakeClass.StubUserAgentUtilClass;
import com.common.infrastructure.exception.CustomBusinessException;
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
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(OutputCaptureExtension.class)
class RegisterAccountServiceTest {

    RegisterAccountService service;
    FakeRoleStoragePort fakeRoleStoragePort;
    FakeRedisStoragePortClass fakeRedisStoragePortClass;
    StubUserAgentUtilClass stubUserAgentUtilClass;
    FakeAccountStorageClass fakeAccountStorageClass;

    RegisterAccountServiceTest() {
        fakeRoleStoragePort = new FakeRoleStoragePort();
        stubUserAgentUtilClass = new StubUserAgentUtilClass();
        fakeAccountStorageClass = new FakeAccountStorageClass();
        fakeRedisStoragePortClass = new FakeRedisStoragePortClass();
        service = new RegisterAccountService(
            stubUserAgentUtilClass,
            fakeRoleStoragePort,
            fakeRedisStoragePortClass,
            fakeAccountStorageClass
        );
        ReflectionTestUtils.setField(service, "tokenRedisKey", "token::%s-%s");
        ReflectionTestUtils.setField(service, "refreshTokenTtl", 99999999L);
    }

    @BeforeEach
    void setup() {
        fakeAccountStorageClass.accountList.clear();
        fakeRedisStoragePortClass.redisData.clear();
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
                .roles(List.of("ROLE_CUSTOMER"))
                .build();

            // when
            RegisterAccountServiceResponse response = service.registerAccount(command);

            // then
            Account savedAccount = fakeAccountStorageClass.accountList.getFirst();
            assert savedAccount.getEmail().equals(command.email());
            assert savedAccount.getAddress().equals(command.address());
            assert savedAccount.getUserTel().equals(command.userTel());
            assert JwtUtil.validateTokenExceptExpiration(response.accessToken());
            assert output.toString().contains("FakeCachePortClass registerToken");
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
                .roles(List.of(Role.builder().id(1L).name("ROLE_CUSTOMER").build()))
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
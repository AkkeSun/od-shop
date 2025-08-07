package com.account.applicaiton.service.register_token;

import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.fakeClass.DummyMessageProducerPortClass;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeRedisStoragePortClass;
import com.account.fakeClass.StubUserAgentUtilClass;
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
class RegisterTokenServiceTest {

    RegisterTokenService service;
    FakeJwtUtilClass fakeJwtUtilClass;
    FakeRedisStoragePortClass fakeRedisStoragePortClass;
    StubUserAgentUtilClass fakeUserAgentUtilClass;
    DummyMessageProducerPortClass dummyMessageProducerPortClass;
    FakeAccountStorageClass fakeAccountStorageClass;

    RegisterTokenServiceTest() {
        fakeJwtUtilClass = new FakeJwtUtilClass();
        fakeRedisStoragePortClass = new FakeRedisStoragePortClass();
        fakeUserAgentUtilClass = new StubUserAgentUtilClass();
        dummyMessageProducerPortClass = new DummyMessageProducerPortClass();
        fakeAccountStorageClass = new FakeAccountStorageClass();

        service = new RegisterTokenService(
            fakeJwtUtilClass,
            fakeUserAgentUtilClass,
            fakeRedisStoragePortClass,
            fakeAccountStorageClass,
            dummyMessageProducerPortClass
        );
        ReflectionTestUtils.setField(service, "loginTopic", "account-login");
        ReflectionTestUtils.setField(service, "tokenRedisKey", "token::%s-%s");
        ReflectionTestUtils.setField(service, "refreshTokenTtl", 99999999L);
    }

    @BeforeEach
    void setup() {
        fakeRedisStoragePortClass.redisData.clear();
    }

    @Nested
    @DisplayName("[registerToken] 토큰을 등록하는 메소드")
    class Describe_registerToken {

        @Test
        @DisplayName("[success] 토큰을 발급 및 저장하고 로그인 로그 메시지를 발송한 후 성공 메시지를 응답한다.")
        void success(CapturedOutput output) {
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

            RegisterTokenCommand command = RegisterTokenCommand.builder()
                .email(account.getEmail())
                .password(account.getPassword())
                .build();

            // when
            RegisterTokenServiceResponse response = service.registerToken(command);

            // then
            assert response.accessToken().equals("valid token - " + account.getEmail());
            assert response.refreshToken().equals("valid refresh token - " + account.getEmail());
            assert output.toString().contains("FakeCachePortClass registerToken");
            assert output.toString().contains("[account-login] ==>");
        }
    }
}
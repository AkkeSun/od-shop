package com.account.applicaiton.service.delete_token;

import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeRedisStoragePortClass;
import com.account.fakeClass.TestPropertiesHelper;
import com.common.infrastructure.resolver.LoginAccountInfo;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class DeleteTokenServiceTest {

    DeleteTokenService service;
    FakeRedisStoragePortClass fakeCachePortClass;
    FakeAccountStorageClass fakeAccountStorageClass;

    DeleteTokenServiceTest() {
        fakeCachePortClass = new FakeRedisStoragePortClass();
        fakeAccountStorageClass = new FakeAccountStorageClass();
        service = new DeleteTokenService(
            TestPropertiesHelper.createRedisProperties(),
            fakeCachePortClass
        );
    }

    @Nested
    @DisplayName("[deleteToken] 토큰을 삭제하는 메소드")
    class Describe_deleteToken {

        @Test
        @DisplayName("[success] 유효한 인증 토큰 이라면 토큰을 삭제하는지 확인한다.")
        void success(CapturedOutput output) {
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

            // when
            DeleteTokenServiceResponse result = service.deleteToken(LoginAccountInfo.builder()
                .email(account.getEmail())
                .build());

            // then
            assert result.result().equals("Y");
            assert output.toString().contains("FakeCachePortClass deleteTokenByEmail");
        }
    }
}
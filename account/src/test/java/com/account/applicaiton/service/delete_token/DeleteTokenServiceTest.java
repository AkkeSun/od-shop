package com.account.applicaiton.service.delete_token;

import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeRedisStoragePortClass;
import com.account.fakeClass.FakeTokenStoragePortClass;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(OutputCaptureExtension.class)
class DeleteTokenServiceTest {

    DeleteTokenService service;
    FakeJwtUtilClass fakeJwtUtilClass;
    FakeRedisStoragePortClass fakeCachePortClass;
    FakeTokenStoragePortClass fakeTokenStoragePortClass;
    FakeAccountStorageClass fakeAccountStorageClass;

    DeleteTokenServiceTest() {
        fakeJwtUtilClass = new FakeJwtUtilClass();
        fakeCachePortClass = new FakeRedisStoragePortClass();
        fakeTokenStoragePortClass = new FakeTokenStoragePortClass();
        fakeAccountStorageClass = new FakeAccountStorageClass();
        service = new DeleteTokenService(
            fakeCachePortClass,
            fakeTokenStoragePortClass
        );
        ReflectionTestUtils.setField(service, "tokenRedisKey", "token::%s-%s");
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
            String authentication = "valid token - " + account.getEmail();

            // when
            DeleteTokenServiceResponse result = service.deleteToken(account);

            // then
            assert result.result().equals("Y");
            assert output.toString().contains("FakeTokenStoragePortClass deleteByEmail");
            assert output.toString().contains("FakeCachePortClass deleteTokenByEmail");
        }
    }
}
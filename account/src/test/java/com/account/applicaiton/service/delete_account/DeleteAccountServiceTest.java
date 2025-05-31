package com.account.applicaiton.service.delete_account;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.fakeClass.DummyMessageProducerPortClass;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeRedisStoragePortClass;
import com.account.fakeClass.FakeTokenStoragePortClass;
import com.account.infrastructure.exception.CustomNotFoundException;
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
class DeleteAccountServiceTest {

    DeleteAccountService service;
    FakeRedisStoragePortClass fakeCachePortClass;
    FakeTokenStoragePortClass fakeTokenStoragePortClass;
    FakeAccountStorageClass fakeAccountStorageClass;
    FakeJwtUtilClass fakeJwtUtilClass;
    DummyMessageProducerPortClass dummyMessageProducerPortClass;

    DeleteAccountServiceTest() {
        fakeCachePortClass = new FakeRedisStoragePortClass();
        fakeTokenStoragePortClass = new FakeTokenStoragePortClass();
        fakeAccountStorageClass = new FakeAccountStorageClass();
        fakeJwtUtilClass = new FakeJwtUtilClass();
        dummyMessageProducerPortClass = new DummyMessageProducerPortClass();

        service = new DeleteAccountService(
            fakeJwtUtilClass,
            fakeCachePortClass,
            fakeTokenStoragePortClass,
            fakeAccountStorageClass,
            dummyMessageProducerPortClass
        );
    }

    @BeforeEach
    void setup() {
        fakeAccountStorageClass.accountList.clear();
    }

    @Nested
    @DisplayName("[deleteAccount] 사용자 정보를 삭제하는 메소드")
    class Describe_deleteAccount {

        @Test
        @DisplayName("[success] 조회된 사용자 정보가 있다면 정보를 삭제하고 메시지를 전송하는지 확인한다.")
        void success(CapturedOutput output) {
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
            String authentication = fakeJwtUtilClass.createAccessToken(account);

            // when
            DeleteAccountServiceResponse response = service.deleteAccount(authentication);

            // then
            assert response.result().equals("Y");
            assert response.id() == 1L;
            assert output.toString().contains("FakeAccountStorageClass deleteById");
            assert output.toString().contains("FakeTokenStoragePortClass deleteByEmail");
            assert output.toString().contains("FakeCachePortClass deleteTokenByEmail");
            assert output.toString().contains("[delete-account] ==>");
            assert output.toString().contains("[account-history] ==>");
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없다면 예외를 응답하는지 확인한다.")
        void error1() {
            // given
            String authentication = "error";

            // when
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> service.deleteAccount(authentication));

            // then
            assert exception.getErrorCode().equals(ErrorCode.DoesNotExist_ACCOUNT_INFO);
        }
    }
}
package com.account.applicaiton.service.delete_account;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.fakeClass.DummyMessageProducerPortClass;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeRedisStoragePortClass;
import com.common.infrastructure.exception.CustomNotFoundException;
import com.common.infrastructure.exception.ErrorCode;
import com.common.infrastructure.resolver.LoginAccountInfo;
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
class DeleteAccountServiceTest {

    DeleteAccountService service;
    FakeRedisStoragePortClass fakeCachePortClass;
    FakeAccountStorageClass fakeAccountStorageClass;
    DummyMessageProducerPortClass dummyMessageProducerPortClass;

    DeleteAccountServiceTest() {
        fakeCachePortClass = new FakeRedisStoragePortClass();
        fakeAccountStorageClass = new FakeAccountStorageClass();
        dummyMessageProducerPortClass = new DummyMessageProducerPortClass();

        service = new DeleteAccountService(
            fakeCachePortClass,
            fakeAccountStorageClass,
            dummyMessageProducerPortClass
        );

        ReflectionTestUtils.setField(service, "historyTopic", "account-history");
        ReflectionTestUtils.setField(service, "deleteTopic", "delete-account");
        ReflectionTestUtils.setField(service, "tokenRedisKey", "token::%s-%s");
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
                .roles(List.of(Role.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .password("1234")
                .build();
            fakeAccountStorageClass.register(account);

            // when
            DeleteAccountServiceResponse response = service.deleteAccount(LoginAccountInfo
                .builder()
                .id(account.getId())
                .email(account.getEmail())
                .roles(List.of("ROLE_CUSTOMER"))
                .build());

            // then
            assert response.result().equals("Y");
            assert response.id() == 1L;
            assert output.toString().contains("FakeAccountStorageClass deleteById");
            assert output.toString().contains("FakeCachePortClass deleteTokenByEmail");
            assert output.toString().contains("[delete-account] ==>");
            assert output.toString().contains("[account-history] ==>");
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없다면 예외를 응답하는지 확인한다.")
        void error1() {
            // when
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> service.deleteAccount(new LoginAccountInfo()));

            // then
            assert exception.getErrorCode().equals(ErrorCode.DoesNotExist_ACCOUNT_INFO);
        }
    }
}
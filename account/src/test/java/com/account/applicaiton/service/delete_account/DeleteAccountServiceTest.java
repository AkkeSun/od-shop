package com.account.applicaiton.service.delete_account;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeCachePortClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeMessageProducerPortClass;
import com.account.fakeClass.FakeTokenStoragePortClass;
import com.account.infrastructure.exception.CustomNotFoundException;
import com.account.infrastructure.exception.ErrorCode;
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

    @BeforeEach
    void setup() {
        service = new DeleteAccountService(
            new FakeJwtUtilClass(),
            new FakeCachePortClass(),
            new FakeTokenStoragePortClass(),
            new FakeAccountStorageClass(),
            new FakeMessageProducerPortClass()
        );
    }

    @Nested
    @DisplayName("[deleteAccount] 사용자 정보를 삭제하는 메소드")
    class Describe_deleteAccount {

        @Test
        @DisplayName("[success] 조회된 사용자 정보가 있다면 정보를 삭제하고 메시지를 전송하는지 확인한다.")
        void success(CapturedOutput output) {
            // given
            String authentication = "valid token";

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
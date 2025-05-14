package com.account.applicaiton.service.update_account;

import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeJwtUtilClass;
import com.account.fakeClass.FakeMessageProducerPortClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class UpdateAccountServiceTest {

    UpdateAccountService updateAccountService;

    @BeforeEach
    void setup() {
        updateAccountService = new UpdateAccountService(
            new FakeJwtUtilClass(),
            new FakeAccountStorageClass(),
            new FakeMessageProducerPortClass()
        );
    }

    @Nested
    @DisplayName("[updateAccount] 사용자 정보를 수정하는 메소드")
    class Describe_updateAccount {

        @Test
        @DisplayName("[success] 저장된 사용자 정보와 입력받은 사용자 정보가 다른 경우 사용자 정보를 수정하고 메시지를 발송한 후 수정된 정보를 반환한다.")
        void success(CapturedOutput output) {
            // given
            UpdateAccountCommand command = UpdateAccountCommand.builder()
                .accessToken("valid token")
                .username("updateAccount.username")
                .password("updateAccount.password")
                .userTel("updateAccount.userTel")
                .address("updateAccount.address")
                .build();

            // when
            UpdateAccountServiceResponse response = updateAccountService.updateAccount(command);

            // then
            assert output.toString().contains("[account-history] ==> ");
            assert output.toString().contains("FakeAccountStorageClass update");
            assert response.updateYn().equals("Y");
            assert response.updateList().contains("username");
            assert response.updateList().contains("password");
            assert response.updateList().contains("userTel");
            assert response.updateList().contains("address");
        }

        @Test
        @DisplayName("[success] 저장된 사용자 정보와 입력받은 사용자 정보가 같은 경우 사용자 정보를 수정하지 않고 응답값을 반환한다.")
        void success2(CapturedOutput output) {
            // given
            UpdateAccountCommand command = UpdateAccountCommand.builder()
                .accessToken("valid token")
                .username("username")
                .password("password")
                .userTel("userTel")
                .address("address")
                .build();

            // when
            UpdateAccountServiceResponse response = updateAccountService.updateAccount(command);

            // then
            assert !output.toString().contains("[account-history] ==> ");
            assert !output.toString().contains("FakeAccountStorageClass update");
            assert response.updateYn().equals("N");
            assert response.updateList().isEmpty();
        }
    }
}
package com.account.applicaiton.service.update_account;

import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.fakeClass.DummyMessageProducerPortClass;
import com.account.fakeClass.FakeAccountStorageClass;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(OutputCaptureExtension.class)
class UpdateAccountServiceTest {

    UpdateAccountService updateAccountService;
    FakeAccountStorageClass fakeAccountStorageClass;
    DummyMessageProducerPortClass dummyMessageProducerPortClass;

    UpdateAccountServiceTest() {
        fakeAccountStorageClass = new FakeAccountStorageClass();
        dummyMessageProducerPortClass = new DummyMessageProducerPortClass();

        updateAccountService = new UpdateAccountService(
            fakeAccountStorageClass,
            dummyMessageProducerPortClass
        );

        ReflectionTestUtils.setField(updateAccountService, "historyTopic", "account-history");
    }

    @BeforeEach
    void setup() {
        fakeAccountStorageClass.accountList.clear();
    }

    @Nested
    @DisplayName("[updateAccount] 사용자 정보를 수정하는 메소드")
    class Describe_updateAccount {

        @Test
        @DisplayName("[success] 저장된 사용자 정보와 입력받은 사용자 정보가 다른 경우 사용자 정보를 수정하고 메시지를 발송한 후 수정된 정보를 반환한다.")
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
            UpdateAccountCommand command = UpdateAccountCommand.builder()
                .accountId(1L)
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
            Account account = Account.builder()
                .id(1L)
                .email("od@test.com")
                .password("1234")
                .username("od")
                .address("address")
                .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .regDate("20240101")
                .userTel("01012341234")
                .role(Role.ROLE_CUSTOMER)
                .password("1234")
                .build();
            fakeAccountStorageClass.register(account);
            UpdateAccountCommand command = UpdateAccountCommand.builder()
                .accountId(1L)
                .username(account.getUsername())
                .password(account.getPassword())
                .userTel(account.getUserTel())
                .address(account.getAddress())
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
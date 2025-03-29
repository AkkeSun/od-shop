package com.account.applicaiton.service.delete_account;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.IntegrationTestSupport;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.infrastructure.exception.CustomNotFoundException;
import com.account.infrastructure.exception.ErrorCode;
import com.account.infrastructure.util.AesUtil;
import com.account.infrastructure.util.JwtUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;

class DeleteAccountServiceTest extends IntegrationTestSupport {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AesUtil aesUtil;
    @Autowired
    AccountStoragePort accountStoragePort;
    @Autowired
    DeleteAccountService service;

    @Nested
    @DisplayName("[deleteAccount] 사용자 정보를 삭제하는 메소드")
    class Describe_deleteAccount {

        @Test
        @DisplayName("[success] 조회된 사용자 정보가 있다면 정보를 삭제하고 메시지를 전송하는지 확인한다.")
        void success(CapturedOutput output) throws InterruptedException {
            // given
            Account account = Account.builder()
                .email("od@test.com")
                .username("od")
                .regDateTime(LocalDateTime.now())
                .regDate("20240101")
                .userTel("01012341234")
                .role(Role.ROLE_CUSTOMER)
                .password(aesUtil.encryptText("1234"))
                .build();
            Account savedAccount = accountStoragePort.register(account);
            String authentication = jwtUtil.createAccessToken(savedAccount);

            // when
            DeleteAccountServiceResponse response = service.deleteAccount(authentication);
            boolean existsByEmail = accountStoragePort.existsByEmail(account.getEmail());
            Thread.sleep(1000);

            // then
            assert !existsByEmail;
            assert response.result().equals("Y");
            assert output.toString().contains("[delete-account] ==>");
            assert output.toString().contains("[account-history] ==>");

            // clean up
            accountStoragePort.deleteById(savedAccount.getId());
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없다면 예외를 응답하는지 확인한다.")
        void error1() {
            // given
            Account account = Account.builder()
                .email("od@test.com")
                .username("od")
                .regDateTime(LocalDateTime.now())
                .regDate("20240101")
                .userTel("01012341234")
                .role(Role.ROLE_CUSTOMER)
                .password(aesUtil.encryptText("1234"))
                .build();
            String authentication = jwtUtil.createAccessToken(account);

            // when
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> service.deleteAccount(authentication));

            // then
            assert exception.getErrorCode().equals(ErrorCode.DoesNotExist_ACCOUNT_INFO);
        }
    }
}
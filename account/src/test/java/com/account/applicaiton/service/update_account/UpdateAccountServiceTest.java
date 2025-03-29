package com.account.applicaiton.service.update_account;

import com.account.IntegrationTestSupport;
import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.infrastructure.util.AesUtil;
import com.account.infrastructure.util.JwtUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;

class UpdateAccountServiceTest extends IntegrationTestSupport {

    @Autowired
    AesUtil aesUtil;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UpdateAccountService updateAccountService;
    @Autowired
    AccountStoragePort accountStoragePort;
    
    @Nested
    @DisplayName("[updateAccount] 사용자 정보를 수정하는 메소드")
    class Describe_updateAccount {

        @Test
        @DisplayName("[success] 저장된 사용자 정보와 입력받은 사용자 정보가 다른 경우 사용자 정보를 수정하고 메시지를 발송한 후 수정된 정보를 반환한다.")
        void success(CapturedOutput output) throws InterruptedException {
            // given
            Account account = Account.builder()
                .username("od")
                .email("od@updateTest.com")
                .password("1234")
                .role(Role.ROLE_SELLER)
                .regDate("20240201")
                .regDateTime(LocalDateTime.now())
                .build();
            Account savedAccount = accountStoragePort.register(account);
            UpdateAccountCommand command = UpdateAccountCommand.builder()
                .accessToken(jwtUtil.createAccessToken(savedAccount))
                .username("updateAccount.username")
                .password("updateAccount.password")
                .userTel("updateAccount.userTel")
                .address("updateAccount.address")
                .build();

            // when
            UpdateAccountServiceResponse response = updateAccountService.updateAccount(command);
            Account updatedAccount = accountStoragePort.findByEmailAndPassword(account.getEmail(),
                command.password());
            Thread.sleep(1000);

            // then
            assert output.toString().contains("[account-history] ==> ");
            assert response.updateYn().equals("Y");
            assert response.updateList().contains("username");
            assert response.updateList().contains("password");
            assert response.updateList().contains("userTel");
            assert response.updateList().contains("address");
            assert updatedAccount.getUsername().equals(command.username());
            assert updatedAccount.getPassword().equals(command.password());
            assert updatedAccount.getUserTel().equals(command.userTel());
            assert updatedAccount.getAddress().equals(command.address());

            // clean
            accountStoragePort.deleteById(savedAccount.getId());
        }

        @Test
        @DisplayName("[success] 저장된 사용자 정보와 입력받은 사용자 정보가 같은 경우 사용자 정보를 수정하지 않고 응답값을 반환한다.")
        void success2(CapturedOutput output) throws InterruptedException {
            // given
            Account account = Account.builder()
                .email("od@updateTest.com")
                .username("od")
                .password("1234")
                .role(Role.ROLE_SELLER)
                .regDate("20240201")
                .address("서울시 강남구")
                .userTel("01012345678")
                .regDateTime(LocalDateTime.now())
                .build();
            Account savedAccount = accountStoragePort.register(account);
            UpdateAccountCommand command = UpdateAccountCommand.builder()
                .accessToken(jwtUtil.createAccessToken(savedAccount))
                .username(account.getUsername())
                .password(account.getPassword())
                .userTel(account.getUserTel())
                .address(account.getAddress())
                .build();
            // when
            UpdateAccountServiceResponse response = updateAccountService.updateAccount(command);
            Account updatedAccount = accountStoragePort.findByEmailAndPassword(
                account.getEmail(), account.getPassword());
            Thread.sleep(1000);

            // then
            assert !output.toString().contains("[account-history] ==> ");
            assert response.updateYn().equals("N");
            assert response.updateList().isEmpty();
            assert savedAccount.getUsername().equals(updatedAccount.getUsername());
            assert savedAccount.getPassword().equals(updatedAccount.getPassword());
            assert savedAccount.getUserTel().equals(updatedAccount.getUserTel());
            assert savedAccount.getAddress().equals(updatedAccount.getAddress());

            // clean
            accountStoragePort.deleteById(savedAccount.getId());
        }
    }
}
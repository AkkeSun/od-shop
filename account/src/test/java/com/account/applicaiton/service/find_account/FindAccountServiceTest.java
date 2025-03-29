package com.account.applicaiton.service.find_account;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.IntegrationTestSupport;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.CustomNotFoundException;
import com.account.infrastructure.exception.ErrorCode;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FindAccountServiceTest extends IntegrationTestSupport {

    @Autowired
    FindAccountService service;
    @Autowired
    AccountStoragePort accountStoragePort;

    @Nested
    @DisplayName("[findAccountInfo] 사용자 정보를 조회하는 메소드")
    class Describe_findAccountInfo {

        @Test
        @DisplayName("[success] 조회된 사용자 정보가 있다면 정보를 반환하는지 확인한다.")
        void success() {

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
            FindAccountServiceResponse response = service.findAccountInfo(authentication);

            // then
            assert response.id().equals(savedAccount.getId());
            assert response.email().equals(savedAccount.getEmail());
            assert response.username().equals(savedAccount.getUsername());
            assert response.userTel().equals(savedAccount.getUserTel());
            assert response.role().equals(savedAccount.getRole().toString());
            assert response.regDate().equals(savedAccount.getRegDate());

            // clean
            accountStoragePort.deleteById(savedAccount.getId());
        }

        @Test
        @DisplayName("[error] 유효한 토큰이 아니라면 예외를 응답한다.")
        void error() {

            // given
            String authentication = "authentication";

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () -> service.findAccountInfo(authentication));

            // then
            assert exception.getErrorCode().equals(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없다면 예외를 응답한다.")
        void error2() {

            // given
            Account account = Account.builder()
                .id(15L)
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
                () -> service.findAccountInfo(authentication));

            // then
            assert exception.getErrorCode().equals(ErrorCode.DoesNotExist_ACCOUNT_INFO);
        }
    }
}
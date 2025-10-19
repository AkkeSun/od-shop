package com.account.applicaiton.service.find_account;

import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.fakeClass.FakeAccountStorageClass;
import com.common.infrastructure.resolver.LoginAccountInfo;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindAccountServiceTest {

    FindAccountService service;
    FakeAccountStorageClass fakeAccountStorageClass;

    FindAccountServiceTest() {
        fakeAccountStorageClass = new FakeAccountStorageClass();
        service = new FindAccountService(
            fakeAccountStorageClass
        );
    }

    @BeforeEach
    void setup() {
        fakeAccountStorageClass.accountList.clear();
    }

    @Nested
    @DisplayName("[findAccountInfo] 사용자 정보를 조회하는 메소드")
    class Describe_findAccountInfo {

        @Test
        @DisplayName("[success] 조회된 사용자 정보가 있다면 정보를 반환하는지 확인한다.")
        void success() {

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
            FindAccountServiceResponse response = service.findAccountInfo(
                LoginAccountInfo.builder()
                    .email(account.getEmail())
                    .id(account.getId())
                    .build());

            // then
            assert response.id().equals(account.getId());
            assert response.email().equals(account.getEmail());
            assert response.username().equals(account.getUsername());
            assert response.userTel().equals(account.getUserTel());
            assert response.roles().contains("ROLE_CUSTOMER");
            assert response.regDate().equals(account.getRegDate());
        }
    }
}
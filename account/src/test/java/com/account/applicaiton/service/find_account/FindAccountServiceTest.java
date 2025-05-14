package com.account.applicaiton.service.find_account;

import com.account.fakeClass.FakeAccountStorageClass;
import com.account.fakeClass.FakeJwtUtilClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindAccountServiceTest {

    FindAccountService service;

    @BeforeEach
    void setup() {
        service = new FindAccountService(
            new FakeJwtUtilClass(),
            new FakeAccountStorageClass()
        );
    }

    @Nested
    @DisplayName("[findAccountInfo] 사용자 정보를 조회하는 메소드")
    class Describe_findAccountInfo {

        @Test
        @DisplayName("[success] 조회된 사용자 정보가 있다면 정보를 반환하는지 확인한다.")
        void success() {

            // given
            String authentication = "valid token";

            // when
            FindAccountServiceResponse response = service.findAccountInfo(authentication);

            // then
            assert response.id().equals(1L);
            assert response.email().equals("email");
            assert response.username().equals("username");
            assert response.userTel().equals("userTel");
            assert response.address().equals("address");
            assert response.role().equals("ROLE_SELLER");
            assert response.regDate().equals("20231010");
        }
    }
}
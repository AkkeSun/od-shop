package com.account.fakeClass;

import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeAccountStorageClass implements AccountStoragePort {

    @Override
    public Account register(Account account) {
        return account;
    }

    @Override
    public Account findById(Long id) {
        return Account.builder()
            .id(id)
            .email("email")
            .password("password")
            .username("username")
            .userTel("userTel")
            .address("address")
            .role(Role.ROLE_SELLER)
            .regDate("20231010")
            .build();
    }

    @Override
    public Account findByEmailAndPassword(String email, String password) {
        return Account.builder()
            .id(1L)
            .email(email)
            .username("username")
            .password("password")
            .userTel("userTel")
            .address("address")
            .role(Role.ROLE_SELLER)
            .regDate("20231010")
            .build();
    }

    @Override
    public void update(Account account) {
        log.info("FakeAccountStorageClass update");
    }

    @Override
    public boolean existsByEmail(String email) {
        return email.equals("success");
    }

    @Override
    public void deleteById(Long id) {
        log.info("FakeAccountStorageClass deleteById");
    }
}

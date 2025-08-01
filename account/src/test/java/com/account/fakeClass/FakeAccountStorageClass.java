package com.account.fakeClass;

import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.domain.model.Account;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeAccountStorageClass implements AccountStoragePort {

    public List<Account> accountList = new ArrayList<>();

    @Override
    public Account register(Account account) {
        accountList.add(account);
        log.info("FakeCachePortClass registerToken");
        return account;
    }

    @Override
    public Account findById(Long id) {
        return accountList.stream()
            .filter(account -> account.getId().equals(id))
            .toList()
            .getFirst();
    }

    @Override
    public Account findByEmailAndPassword(String email, String password) {
        return accountList.stream()
            .filter(account -> account.getEmail().equals(email))
            .filter(account -> account.getPassword().equals(password))
            .toList()
            .getFirst();
    }

    @Override
    public void update(Account account) {
        log.info("FakeAccountStorageClass update");
    }

    @Override
    public boolean existsByEmail(String email) {
        return !accountList.stream()
            .filter(account -> account.getEmail().equals(email))
            .toList().isEmpty();
    }

    @Override
    public void deleteById(Long id) {
        accountList.remove(findById(id));
        log.info("FakeAccountStorageClass deleteById");
    }
}

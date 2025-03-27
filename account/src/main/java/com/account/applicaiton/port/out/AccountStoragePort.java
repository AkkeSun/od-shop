package com.account.applicaiton.port.out;

import com.account.domain.model.Account;

public interface AccountStoragePort {

    Account register(Account account);

    Account findById(Long id);

    Account findByEmailAndPassword(String email, String password);

    void update(Account account);

    boolean existsByEmail(String email);

    void deleteById(Long id);
}

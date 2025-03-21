package com.account.account.applicaiton.port.out;

import com.account.account.domain.model.Account;

public interface FindAccountPort {

    Account findById(Long id);

    Account findByEmail(String email);

    Account findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
}

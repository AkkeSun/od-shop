package com.account.applicaiton.port.out;

import com.account.domain.model.Account;

public interface FindAccountPort {

    Account findById(Long id);

    Account findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
}

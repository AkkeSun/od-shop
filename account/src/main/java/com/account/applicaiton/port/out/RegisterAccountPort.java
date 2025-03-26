package com.account.applicaiton.port.out;

import com.account.domain.model.Account;

public interface RegisterAccountPort {

    Account register(Account account);
}

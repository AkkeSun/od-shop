package com.account.account.applicaiton.port.out;

import com.account.account.domain.model.Account;

public interface RegisterAccountPort {

    Account register(Account account);
}

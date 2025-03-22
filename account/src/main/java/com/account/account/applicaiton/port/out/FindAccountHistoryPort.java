package com.account.account.applicaiton.port.out;

import com.account.account.domain.model.AccountHistory;

public interface FindAccountHistoryPort {

    AccountHistory findByEmail(String email);
}

package com.account.applicaiton.port.out;

import com.account.domain.model.AccountHistory;

public interface FindAccountHistoryPort {

    AccountHistory findByEmail(String email);
}

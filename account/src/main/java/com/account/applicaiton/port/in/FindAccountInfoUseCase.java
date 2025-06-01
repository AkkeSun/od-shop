package com.account.applicaiton.port.in;

import com.account.applicaiton.service.find_account.FindAccountServiceResponse;
import com.account.domain.model.Account;

public interface FindAccountInfoUseCase {

    FindAccountServiceResponse findAccountInfo(Account account);
}

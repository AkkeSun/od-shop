package com.account.applicaiton.port.in;

import com.account.applicaiton.service.delete_account.DeleteAccountServiceResponse;
import com.account.domain.model.Account;

public interface DeleteAccountUseCase {

    DeleteAccountServiceResponse deleteAccount(Account account);
}

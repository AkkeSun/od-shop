package com.account.account.applicaiton.port.in;

import com.account.account.applicaiton.service.delete_account.DeleteAccountServiceResponse;

public interface DeleteAccountUseCase {

    DeleteAccountServiceResponse deleteAccount(String authentication);
}

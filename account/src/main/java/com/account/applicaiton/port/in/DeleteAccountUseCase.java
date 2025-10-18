package com.account.applicaiton.port.in;

import com.account.applicaiton.service.delete_account.DeleteAccountServiceResponse;
import com.common.infrastructure.resolver.LoginAccountInfo;

public interface DeleteAccountUseCase {

    DeleteAccountServiceResponse deleteAccount(LoginAccountInfo loginInfo);
}

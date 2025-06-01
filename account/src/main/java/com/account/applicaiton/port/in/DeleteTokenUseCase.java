package com.account.applicaiton.port.in;

import com.account.applicaiton.service.delete_token.DeleteTokenServiceResponse;
import com.account.domain.model.Account;

public interface DeleteTokenUseCase {

    DeleteTokenServiceResponse deleteToken(Account account);
}

package com.account.applicaiton.port.in;

import com.account.applicaiton.service.delete_token.DeleteTokenServiceResponse;
import com.common.infrastructure.resolver.LoginAccountInfo;

public interface DeleteTokenUseCase {

    DeleteTokenServiceResponse deleteToken(LoginAccountInfo loginInfo);
}

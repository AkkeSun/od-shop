package com.account.applicaiton.port.in;

import com.account.applicaiton.service.find_account.FindAccountServiceResponse;
import com.common.infrastructure.resolver.LoginAccountInfo;

public interface FindAccountInfoUseCase {

    FindAccountServiceResponse findAccountInfo(LoginAccountInfo loginInfo);
}

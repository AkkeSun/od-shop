package com.account.account.applicaiton.port.in;

import com.account.account.applicaiton.service.find_account.FindAccountServiceResponse;

public interface FindAccountInfoUseCase {

    FindAccountServiceResponse findAccountInfo(String Authorization);
}

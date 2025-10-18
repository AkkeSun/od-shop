package com.account.applicaiton.service.find_account;

import com.account.applicaiton.port.in.FindAccountInfoUseCase;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.domain.model.Account;
import com.common.infrastructure.resolver.LoginAccountInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindAccountService implements FindAccountInfoUseCase {

    private final AccountStoragePort accountStoragePort;

    @Override
    public FindAccountServiceResponse findAccountInfo(LoginAccountInfo loginInfo) {
        Account savedAccount = accountStoragePort.findById(loginInfo.getId());
        return FindAccountServiceResponse.of(savedAccount);
    }
}

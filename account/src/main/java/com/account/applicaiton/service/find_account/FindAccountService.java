package com.account.applicaiton.service.find_account;

import com.account.applicaiton.port.in.FindAccountInfoUseCase;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.domain.model.Account;
import com.account.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindAccountService implements FindAccountInfoUseCase {

    private final JwtUtil jwtUtil;
    private final AccountStoragePort accountStoragePort;

    @Override
    public FindAccountServiceResponse findAccountInfo(Account account) {
        Account savedAccount = accountStoragePort.findById(account.getId());
        return FindAccountServiceResponse.of(savedAccount);
    }
}

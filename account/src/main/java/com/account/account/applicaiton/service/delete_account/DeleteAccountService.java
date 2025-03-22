package com.account.account.applicaiton.service.delete_account;

import com.account.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.account.applicaiton.port.out.DeleteAccountPort;
import com.account.account.applicaiton.port.out.ProduceAccountPort;
import com.account.account.applicaiton.port.out.RegisterAccountHistoryPort;
import com.account.account.domain.model.AccountHistory;
import com.account.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteAccountService implements DeleteAccountUseCase {

    private final JwtUtil jwtUtil;
    private final DeleteAccountPort deleteAccountPort;
    private final RegisterAccountHistoryPort registerAccountHistoryPort;
    private final ProduceAccountPort produceAccountPort;

    @Override
    public DeleteAccountServiceResponse deleteAccount(String authentication) {

        Long accountId = jwtUtil.getAccountId(authentication);
        AccountHistory history = new AccountHistory().createAccountHistoryForDelete(accountId);
        registerAccountHistoryPort.register(history);

        deleteAccountPort.deleteById(accountId);
        produceAccountPort.sendMessage("delete-account", String.valueOf(accountId)); // TODO: 토큰 삭제

        return DeleteAccountServiceResponse.builder()
            .id(accountId)
            .result("Y")
            .build();
    }
}

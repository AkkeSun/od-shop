package com.account.applicaiton.service.delete_account;

import static com.account.domain.model.AccountHistory.createAccountHistoryForDelete;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.MessageProducerPort;
import com.account.domain.model.AccountHistory;
import com.account.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteAccountService implements DeleteAccountUseCase {

    private final JwtUtil jwtUtil;
    private final AccountStoragePort accountStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    public DeleteAccountServiceResponse deleteAccount(String authentication) {

        Long accountId = jwtUtil.getAccountId(authentication);
        AccountHistory history = createAccountHistoryForDelete(accountId);
        accountStoragePort.deleteById(accountId);
        messageProducerPort.sendMessage("delete-account", String.valueOf(accountId)); // TODO: 토큰 삭제
        messageProducerPort.sendMessage("account-history", toJsonString(history));
        return DeleteAccountServiceResponse.builder()
            .id(accountId)
            .result("Y")
            .build();
    }
}

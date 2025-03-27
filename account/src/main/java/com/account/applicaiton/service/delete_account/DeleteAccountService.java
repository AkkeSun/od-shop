package com.account.applicaiton.service.delete_account;

import static com.account.domain.model.AccountHistory.createAccountHistoryForDelete;
import static com.account.infrastructure.exception.ErrorCode.DoesNotExist_ACCOUNT_INFO;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.CachePort;
import com.account.applicaiton.port.out.MessageProducerPort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.AccountHistory;
import com.account.infrastructure.exception.CustomNotFoundException;
import com.account.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteAccountService implements DeleteAccountUseCase {

    private final JwtUtil jwtUtil;
    private final CachePort cachePort;
    private final TokenStoragePort tokenStoragePort;
    private final AccountStoragePort accountStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    public DeleteAccountServiceResponse deleteAccount(String authentication) {

        String email = jwtUtil.getEmail(authentication);

        if (accountStoragePort.existsByEmail(email)) {
            throw new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO);
        }

        Long accountId = jwtUtil.getAccountId(authentication);
        AccountHistory history = createAccountHistoryForDelete(accountId);

        accountStoragePort.deleteById(accountId);
        tokenStoragePort.deleteByEmail(email);
        cachePort.deleteTokenByEmail(email);

        messageProducerPort.sendMessage("delete-account", String.valueOf(accountId));
        messageProducerPort.sendMessage("account-history", toJsonString(history));

        return DeleteAccountServiceResponse.builder()
            .id(accountId)
            .result("Y")
            .build();
    }
}

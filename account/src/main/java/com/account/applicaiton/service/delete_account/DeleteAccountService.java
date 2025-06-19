package com.account.applicaiton.service.delete_account;

import static com.account.domain.model.AccountHistory.createAccountHistoryForDelete;
import static com.account.infrastructure.exception.ErrorCode.DoesNotExist_ACCOUNT_INFO;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.MessageProducerPort;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.AccountHistory;
import com.account.infrastructure.exception.CustomNotFoundException;
import com.account.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteAccountService implements DeleteAccountUseCase {

    @Value("${kafka.topic.history}")
    private String historyTopic;
    @Value("${kafka.topic.delete}")
    private String deleteTopic;
    private final JwtUtil jwtUtil;
    private final RedisStoragePort redisStoragePort;
    private final TokenStoragePort tokenStoragePort;
    private final AccountStoragePort accountStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    public DeleteAccountServiceResponse deleteAccount(Account account) {

        if (!accountStoragePort.existsByEmail(account.getEmail())) {
            throw new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO);
        }

        AccountHistory history = createAccountHistoryForDelete(account.getId());

        accountStoragePort.deleteById(account.getId());
        tokenStoragePort.deleteByEmail(account.getEmail());
        redisStoragePort.deleteTokenByEmail(account.getEmail());

        messageProducerPort.sendMessage(deleteTopic, String.valueOf(account.getId()));
        messageProducerPort.sendMessage(historyTopic, toJsonString(history));

        return DeleteAccountServiceResponse.ofSuccess(account);
    }
}

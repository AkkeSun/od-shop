package com.account.applicaiton.service.delete_account;

import static com.account.infrastructure.exception.ErrorCode.DoesNotExist_ACCOUNT_INFO;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.MessageProducerPort;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.AccountHistory;
import com.account.domain.model.DeleteAccountLog;
import com.account.infrastructure.exception.CustomNotFoundException;
import java.util.List;
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
    @Value("${spring.data.redis.key.token}")
    private String tokenRedisKey;
    private final RedisStoragePort redisStoragePort;
    private final AccountStoragePort accountStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    public DeleteAccountServiceResponse deleteAccount(Account account) {

        if (!accountStoragePort.existsByEmail(account.getEmail())) {
            throw new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO);
        }

        accountStoragePort.deleteById(account.getId());

        List<String> keys = redisStoragePort.getKeys(
            String.format(tokenRedisKey, account.getEmail(), "*"));
        redisStoragePort.delete(keys);

        messageProducerPort.sendMessage(deleteTopic, toJsonString(DeleteAccountLog.of(account)));
        messageProducerPort.sendMessage(historyTopic,
            toJsonString(AccountHistory.createAccountHistoryForDelete(account.getId())));

        return DeleteAccountServiceResponse.ofSuccess(account);
    }
}

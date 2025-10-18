package com.account.applicaiton.service.delete_account;

import static com.common.infrastructure.exception.ErrorCode.DoesNotExist_ACCOUNT_INFO;
import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.MessageProducerPort;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.domain.model.AccountHistory;
import com.account.domain.model.DeleteAccountLog;
import com.common.infrastructure.exception.CustomNotFoundException;
import com.common.infrastructure.resolver.LoginAccountInfo;
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
    public DeleteAccountServiceResponse deleteAccount(LoginAccountInfo loginInfo) {

        if (!accountStoragePort.existsByEmail(loginInfo.getEmail())) {
            throw new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO);
        }

        accountStoragePort.deleteById(loginInfo.getId());

        List<String> keys = redisStoragePort.getKeys(
            String.format(tokenRedisKey, loginInfo.getEmail(), "*"));
        redisStoragePort.delete(keys);

        messageProducerPort.sendMessage(deleteTopic, toJsonString(DeleteAccountLog.of(loginInfo)));
        messageProducerPort.sendMessage(historyTopic,
            toJsonString(AccountHistory.createAccountHistoryForDelete(loginInfo.getId())));

        return DeleteAccountServiceResponse.ofSuccess(loginInfo);
    }
}

package com.account.applicaiton.service.update_account;

import static com.account.domain.model.AccountHistory.createAccountHistoryForUpdate;
import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.UpdateAccountUseCase;
import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.MessageProducerPort;
import com.account.domain.model.Account;
import com.account.domain.model.AccountHistory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class UpdateAccountService implements UpdateAccountUseCase {

    @Value("${kafka.topic.history}")
    private String historyTopic;
    private final AccountStoragePort accountStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    public UpdateAccountServiceResponse updateAccount(UpdateAccountCommand command) {
        Account account = accountStoragePort.findById(command.accountId());

        List<String> updateList = account.update(command);
        if (updateList.isEmpty()) {
            return UpdateAccountServiceResponse.ofFailure(updateList);
        }

        AccountHistory history = createAccountHistoryForUpdate(command.accountId(),
            String.join(",", updateList));

        accountStoragePort.update(account);
        messageProducerPort.sendMessage(historyTopic, toJsonString(history));

        return UpdateAccountServiceResponse.ofSuccess(updateList);
    }
}

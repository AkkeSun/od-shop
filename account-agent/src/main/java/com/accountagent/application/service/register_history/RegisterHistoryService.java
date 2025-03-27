package com.accountagent.application.service.register_history;

import static com.accountagent.global.util.JsonUtil.parseJson;

import com.accountagent.application.port.in.RegisterHistoryUseCase;
import com.accountagent.application.port.out.LogStoragePort;
import com.accountagent.domain.model.AccountHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterHistoryService implements RegisterHistoryUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public RegisterHistoryServiceResponse registerHistory(String payload) {
        AccountHistory accountHistory = parseJson(payload, AccountHistory.class);
        logStoragePort.registerHistoryLog(accountHistory);
        return RegisterHistoryServiceResponse.builder().result("Y").build();
    }
}

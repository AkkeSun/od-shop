package com.accountagent.application.service.register_history_by_polling;

import com.accountagent.application.port.in.RegisterHistoryByPollingUseCase;
import com.accountagent.application.port.out.CachePort;
import com.accountagent.application.port.out.LogStoragePort;
import com.accountagent.domain.model.AccountHistory;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class RegisterHistoryByPollingService implements RegisterHistoryByPollingUseCase {

    private final CachePort cachePort;
    private final LogStoragePort logStoragePort;

    @Override
    public void registerHistory() {
        Map<String, AccountHistory> hostoryMap = cachePort.findAllAccountHistory();
        if (hostoryMap.isEmpty()) {
            return;
        }

        log.info("Register AccountHistory by Polling - " + hostoryMap.size());

        hostoryMap.forEach((k, v) -> {
            logStoragePort.registerHistoryLog(v);
            cachePort.delete(k);
        });
    }
}

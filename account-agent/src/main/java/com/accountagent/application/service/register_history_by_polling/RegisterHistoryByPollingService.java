package com.accountagent.application.service.register_history_by_polling;

import com.accountagent.application.port.in.RegisterHistoryByPollingUseCase;
import com.accountagent.application.port.out.RedisPort;
import com.accountagent.application.port.out.RegisterLogPort;
import com.accountagent.domain.model.AccountHistory;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class RegisterHistoryByPollingService implements RegisterHistoryByPollingUseCase {

    private final RedisPort redisPort;
    private final RegisterLogPort registerLogPort;

    @Override
    // TODO: 분산락
    public void registerHistory() {
        Map<String, AccountHistory> hostoryMap = redisPort.findAllAccountHistory();
        if (hostoryMap.isEmpty()) {
            return;
        }

        log.info("Register AccountHistory by Polling - " + hostoryMap.size());

        hostoryMap.forEach((k, v) -> {
            registerLogPort.registerHistoryLog(v);
            redisPort.delete(k);
        });
    }
}

package com.accountagent.adapter.in.scheduler;

import com.accountagent.application.port.in.RegisterHistoryByPollingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RegisterHistoryScheduler {

    private final RegisterHistoryByPollingUseCase registerHistoryByPollingUseCase;

    @Scheduled(fixedRate = 1000)
    void registerHistory() {
        registerHistoryByPollingUseCase.registerHistory();
    }
}

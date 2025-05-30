package com.accountagent.adapter.in.scheduler;

import com.accountagent.application.port.in.RegisterHistoryByPollingUseCase;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RegisterHistoryScheduler {

    private final RegisterHistoryByPollingUseCase registerHistoryByPollingUseCase;

    @Scheduled(fixedRate = 30000)
    @SchedulerLock(name = "register-history-lock", lockAtLeastFor = "1s", lockAtMostFor = "10s")
    void registerHistory() {
        registerHistoryByPollingUseCase.registerHistory();
    }
}

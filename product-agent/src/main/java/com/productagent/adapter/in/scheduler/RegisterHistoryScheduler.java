package com.productagent.adapter.in.scheduler;

import com.productagent.application.port.in.RegisterHistoryUseCase;
import com.productagent.infrastructure.handler.KafkaBatchHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RegisterHistoryScheduler {

    private final KafkaBatchHandler batchHandler;
    private final RegisterHistoryUseCase registerHistoryUseCase;

    @Scheduled(cron = "0 0/1 * * * *")
    @SchedulerLock(name = "product-history", lockAtLeastFor = "1s", lockAtMostFor = "30s")
    void registerHistory() {
        batchHandler.handle("product-history", registerHistoryUseCase::register);
    }
}

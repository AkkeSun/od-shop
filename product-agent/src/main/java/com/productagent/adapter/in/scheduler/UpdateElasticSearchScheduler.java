package com.productagent.adapter.in.scheduler;

import com.productagent.application.port.in.UpdateElasticSearchUseCase;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UpdateElasticSearchScheduler {

    private final UpdateElasticSearchUseCase useCase;

    @Async
    @Scheduled(cron = "0 */10 * * * *")
    @SchedulerLock(name = "product-es-update", lockAtLeastFor = "1s", lockAtMostFor = "30s")
    void registerSearchLog() {
        useCase.update();
    }
}

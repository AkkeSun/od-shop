package com.productagent.adapter.in.scheduler;

import com.productagent.application.port.in.UpdateProductMetricUseCase;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UpdateProductMetricScheduler {

    private final UpdateProductMetricUseCase useCase;

    @Async
    @Scheduled(cron = "0 */10 * * * *")
    @SchedulerLock(name = "product-metric-update", lockAtLeastFor = "1s", lockAtMostFor = "30s")
    void update() {
        useCase.update();
    }
}

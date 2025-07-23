package com.productagent.adapter.in.scheduler;

import com.productagent.application.port.in.DeleteLogUseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DeleteLogScheduler {

    private final DeleteLogUseCase useCase;

    @Scheduled(cron = "0 0 6 * * *")
    @SchedulerLock(name = "delete-log", lockAtLeastFor = "1s", lockAtMostFor = "30s")
    void update() {
        useCase.delete(LocalDateTime.now().minusDays(90));
    }
}

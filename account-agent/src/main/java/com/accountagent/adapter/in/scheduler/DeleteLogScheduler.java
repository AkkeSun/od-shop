package com.accountagent.adapter.in.scheduler;

import com.accountagent.application.port.in.DeleteLogUseCase;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DeleteLogScheduler {

    private final DeleteLogUseCase deleteLogUseCase;

    @Scheduled(cron = "0 0 7 * * *")
    void deleteLog() {
        String regDate = LocalDate.now().minusDays(90)
            .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        deleteLogUseCase.deleteLog(regDate);
    }
}

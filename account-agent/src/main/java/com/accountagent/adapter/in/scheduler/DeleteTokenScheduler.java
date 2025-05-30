package com.accountagent.adapter.in.scheduler;

import com.accountagent.application.port.in.DeleteTokenUseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DeleteTokenScheduler {

    private final DeleteTokenUseCase deleteTokenUseCase;

    @Scheduled(cron = "0 0 8 * * *")
    void deleteToken() {
        deleteTokenUseCase.deleteToken(LocalDateTime.now().minusDays(3));
    }
}

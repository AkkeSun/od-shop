package com.accountagent.adapter.in.consumer;

import com.accountagent.application.port.in.RegisterDqlInfoUseCase;
import com.accountagent.application.port.in.RegisterHistoryUseCase;
import com.accountagent.application.service.register_history.RegisterHistoryServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RegisterHistoryConsumer {

    private final RegisterHistoryUseCase registerHistoryUseCase;
    private final RegisterDqlInfoUseCase registerDqlInfoUseCase;

    @KafkaListener(
        topics = "account-history",
        containerFactory = "kafkaListenerContainerFactory",
        concurrency = "1"
    )
    void registerHistory(@Payload String payload) {
        log.info("[account-history] <== {}", payload);
        RegisterHistoryServiceResponse response = registerHistoryUseCase.registerHistory(payload);
        log.info("[account-history] result - {}", response.result());
    }

    @KafkaListener(
        topics = "account-history-dlq",
        containerFactory = "kafkaListenerContainerFactory",
        concurrency = "1"
    )
    void registerHistoryDlq(@Payload String payload) {
        try {
            log.info("[account-history-dql] <== {}", payload);
            RegisterHistoryServiceResponse response = registerHistoryUseCase
                .registerHistory(payload);
            log.info("[account-history-dql] result - {}", response.result());
        } catch (Exception e) {
            log.error("[account-history-dlq] error - ", e);
            registerDqlInfoUseCase.registerDqlInfo("account-history", payload);
        }
    }
}

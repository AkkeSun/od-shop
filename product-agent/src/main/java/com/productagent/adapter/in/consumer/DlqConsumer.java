package com.productagent.adapter.in.consumer;

import com.productagent.application.port.in.RegisterDlqUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class DlqConsumer {

    private final RegisterDlqUseCase registerDqlInfoUseCase;

    @KafkaListener(
        topics = "product-dlq",
        containerFactory = "dlqContainerFactory",
        concurrency = "1"
    )
    void registerHistoryDlq(@Payload String payload) {
        log.info("[product-dlq] <== {}", payload);
        registerDqlInfoUseCase.register(payload);
    }
}

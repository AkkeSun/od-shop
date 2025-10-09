package com.productagent.adapter.in.consumer;

import com.productagent.application.port.in.DeleteProductUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class DeleteProductConsumer {

    private final DeleteProductUseCase deleteProductUseCase;

    @KafkaListener(
        topics = "delete-product",
        containerFactory = "defaultContainerFactory",
        concurrency = "1"
    )
    void deleteProduct(@Payload String payload) {
        log.info("[delete-product] <== {}", payload);
        deleteProductUseCase.delete(payload);
    }
}

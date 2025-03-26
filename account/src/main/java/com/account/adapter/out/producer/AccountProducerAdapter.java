package com.account.adapter.out.producer;

import com.account.applicaiton.port.out.ProduceAccountPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AccountProducerAdapter implements ProduceAccountPort {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Async
    @Override
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message)
            .thenAccept(sendResult -> log.info("[{}] ==> {}", topic, message))
            .exceptionally(ex -> {
                log.error("[{}] failed ==> {} | {}", topic, message, ex.toString());
                return null;
            });
    }
}

package com.productagent.infrastructure.handler;

import com.productagent.application.port.in.RegisterDlqUseCase;
import java.time.Duration;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBatchHandlerImpl implements KafkaBatchHandler {

    private final RegisterDlqUseCase registerDlqUseCase;
    private final ConsumerFactory<String, String> batchConsumerFactory;

    @Override
    public void handle(String topic, KafkaBatchProcess handler) {
        Consumer<String, String> consumer = batchConsumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList(topic));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));

        if (records.isEmpty()) {
            return;
        }

        log.info("[{}] <== {}", topic, records.count());

        int maxRetry = 3;
        for (int i = 1; i <= maxRetry; i++) {
            try {
                handler.process(records);
                return;
            } catch (Exception e) {
                threadSleep();
                if (i == maxRetry) {
                    registerDlqUseCase.register(records);
                }
            }
        }

        consumer.commitSync();
        consumer.close();
    }

    private void threadSleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

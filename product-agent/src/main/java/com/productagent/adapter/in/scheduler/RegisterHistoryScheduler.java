package com.productagent.adapter.in.scheduler;

import com.productagent.application.port.in.RegisterHistoryUseCase;
import com.productagent.application.port.out.MessageProducerPort;
import java.time.Duration;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class RegisterHistoryScheduler {

    private final Consumer<String, String> consumer;
    private final RegisterHistoryUseCase registerHistoryUseCase;
    private final MessageProducerPort messageProducerPort;

    RegisterHistoryScheduler(RegisterHistoryUseCase registerHistoryUseCase,
        ConsumerFactory<String, String> batchConsumerFactory,
        MessageProducerPort messageProducerPort) {
        this.registerHistoryUseCase = registerHistoryUseCase;
        this.messageProducerPort = messageProducerPort;
        this.consumer = batchConsumerFactory.createConsumer();
        this.consumer.subscribe(Collections.singletonList("product-history"));
    }

    @Scheduled(cron = "0 0/1 * * * *")
    @SchedulerLock(name = "register-history-lock", lockAtLeastFor = "1s", lockAtMostFor = "30s")
    void registerHistory() throws InterruptedException {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
        if (records.isEmpty()) {
            return;
        }

        log.info("[register-history] <== {}", records.count());

        int maxRetry = 3;
        int retryCount = 0;
        while (retryCount < maxRetry) {
            try {
                registerHistoryUseCase.register(records);
                break;
            } catch (Exception e) {
                retryCount++;
                Thread.sleep(2000);
                if (retryCount == maxRetry) {
                    sendDlqMessage(records);
                }
            }
        }
        consumer.commitSync();
    }

    private void sendDlqMessage(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            String payload = String.format(
                "{ \"topic\": \"%s\", \"value\": \"%s\" }",
                record.topic(), record.value()
            );
            messageProducerPort.sendMessage("product-dlq", payload);
        }
    }
}

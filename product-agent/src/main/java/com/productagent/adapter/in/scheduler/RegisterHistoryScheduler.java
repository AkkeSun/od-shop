package com.productagent.adapter.in.scheduler;

import com.productagent.application.port.in.RegisterHistoryUseCase;
import com.productagent.infrastructure.handler.KafkaBatchHandler;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class RegisterHistoryScheduler {

    private final String topicName;
    private final KafkaBatchHandler batchHandler;
    private final Consumer<String, String> consumer;
    private final RegisterHistoryUseCase registerHistoryUseCase;

    RegisterHistoryScheduler(ConsumerFactory<String, String> registerHistoryConsumerFactory,
        KafkaBatchHandler batchHandler,
        RegisterHistoryUseCase registerHistoryUseCase,
        @Value("${kafka.topic.history}") String topicName) {
        this.topicName = topicName;
        this.batchHandler = batchHandler;
        this.registerHistoryUseCase = registerHistoryUseCase;
        this.consumer = registerHistoryConsumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList(topicName));
    }

    @Async
    @Scheduled(cron = "0 0/1 * * * *")
    @SchedulerLock(name = "product-history", lockAtLeastFor = "1s", lockAtMostFor = "30s")
    void registerHistory() {
        batchHandler.handle(consumer, topicName, registerHistoryUseCase::register);
    }
}

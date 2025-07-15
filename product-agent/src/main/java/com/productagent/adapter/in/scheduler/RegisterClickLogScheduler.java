package com.productagent.adapter.in.scheduler;

import com.productagent.application.port.in.RegisterClickLogUseCase;
import com.productagent.infrastructure.handler.KafkaBatchHandler;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class RegisterClickLogScheduler {

    private final KafkaBatchHandler batchHandler;
    private final Consumer<String, String> consumer;
    private final RegisterClickLogUseCase registerClickLogUseCase;

    RegisterClickLogScheduler(ConsumerFactory<String, String> registerClickConsumerFactory,
        KafkaBatchHandler batchHandler,
        RegisterClickLogUseCase registerClickLogUseCase) {
        this.batchHandler = batchHandler;
        this.registerClickLogUseCase = registerClickLogUseCase;
        this.consumer = registerClickConsumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList("product-click"));
    }

    @Scheduled(cron = "0 0/1 * * * *")
    @SchedulerLock(name = "product-click", lockAtLeastFor = "1s", lockAtMostFor = "30s")
    void registerHistory() {
        batchHandler.handle(consumer, "product-click", registerClickLogUseCase::register);
    }
}

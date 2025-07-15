package com.productagent.infrastructure.handler;

import org.apache.kafka.clients.consumer.Consumer;

public interface KafkaBatchHandler {

    void handle(Consumer<String, String> consumer, String topic, KafkaBatchProcess handler);
}

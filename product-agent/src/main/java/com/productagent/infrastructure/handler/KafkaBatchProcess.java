package com.productagent.infrastructure.handler;

import org.apache.kafka.clients.consumer.ConsumerRecords;

@FunctionalInterface
public interface KafkaBatchProcess {

    void process(ConsumerRecords<String, String> records);
}

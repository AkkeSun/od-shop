package com.productagent.infrastructure.handler;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface DlqHandler {

    void handle(ConsumerRecord<?, ?> record, Exception exception);
}

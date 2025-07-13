package com.productagent.application.port.in;

import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface RegisterHistoryUseCase {

    void register(ConsumerRecords<String, String> records);
}

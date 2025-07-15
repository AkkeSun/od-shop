package com.productagent.application.port.in;

import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface RegisterSearchLogUseCase {

    void register(ConsumerRecords<String, String> records);
}

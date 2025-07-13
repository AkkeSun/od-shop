package com.productagent.application.port.in;

import com.productagent.application.port.in.command.RegisterDlqCommand;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface RegisterDlqUseCase {

    void register(RegisterDlqCommand command);

    void register(ConsumerRecords<String, String> records);

}

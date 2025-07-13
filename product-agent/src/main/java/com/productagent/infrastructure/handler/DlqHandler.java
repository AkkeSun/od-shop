package com.productagent.infrastructure.handler;

import com.productagent.application.port.in.RegisterDlqUseCase;
import com.productagent.application.port.in.command.RegisterDlqCommand;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DlqHandler {

    private final RegisterDlqUseCase registerDlqUseCase;

    public void handle(ConsumerRecord<?, ?> record, Exception exception) {
        registerDlqUseCase.register(RegisterDlqCommand.builder()
            .topic(record.topic())
            .payload((String) record.value())
            .build());
    }
}

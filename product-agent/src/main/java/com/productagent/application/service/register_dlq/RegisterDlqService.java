package com.productagent.application.service.register_dlq;

import com.productagent.application.port.in.RegisterDlqUseCase;
import com.productagent.application.port.in.command.RegisterDlqCommand;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.DlqLog;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterDlqService implements RegisterDlqUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public void register(RegisterDlqCommand command) {
        logStoragePort.registerDlqLog(command.toDomain());
    }

    @Override
    public void register(ConsumerRecords<String, String> records) {
        List<DlqLog> logs = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            logs.add(DlqLog.of(record));
        }
        logStoragePort.registerDlqLogs(logs);
    }
}

package com.productagent.application.service;

import static com.productagent.infrastructure.util.JsonUtil.parseJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productagent.application.port.in.RegisterSearchLogUseCase;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.SearchLog;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class RegisterSearchLogService implements RegisterSearchLogUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public void register(ConsumerRecords<String, String> records) {
        List<SearchLog> logs = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            String payload = record.value();
            try {
                logs.add(parseJson(payload, SearchLog.class));
            } catch (JsonProcessingException e) {
                log.error("[registerSearchLog] parsingFailed - {} ", payload);
            }
        }

        if (!logs.isEmpty()) {
            logStoragePort.registerSearchLogs(logs);
        }

    }
}

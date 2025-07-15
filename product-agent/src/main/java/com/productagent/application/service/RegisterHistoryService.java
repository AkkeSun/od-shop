package com.productagent.application.service;

import static com.productagent.infrastructure.util.JsonUtil.parseJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productagent.application.port.in.RegisterHistoryUseCase;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.ProductHistory;
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
class RegisterHistoryService implements RegisterHistoryUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public void register(ConsumerRecords<String, String> records) {
        List<ProductHistory> histories = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            String payload = record.value();
            try {
                histories.add(parseJson(payload, ProductHistory.class));
            } catch (JsonProcessingException e) {
                log.error("[registerHistory] parsingFailed - {} ", payload);
            }
        }

        if (!histories.isEmpty()) {
            logStoragePort.registerHistories(histories);
        }
        throw new RuntimeException();
    }
}

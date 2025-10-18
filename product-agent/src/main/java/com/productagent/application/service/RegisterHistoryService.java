package com.productagent.application.service;

import static com.common.infrastructure.util.JsonUtil.parseJson;

import com.productagent.application.port.in.RegisterHistoryUseCase;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.ProductHistory;
import com.productagent.infrastructure.constant.CollectionName;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
            ProductHistory history = parseJson(payload, ProductHistory.class);

            if (ObjectUtils.isEmpty(history)) {
                log.error("[registerHistory] parsingFailed - {} ", payload);
                continue;
            }
            histories.add(history);
        }

        if (!histories.isEmpty()) {
            logStoragePort.register(histories, CollectionName.HISTORY());
        }
    }
}

package com.productagent.application.service;

import static com.common.infrastructure.util.JsonUtil.parseJson;

import com.productagent.application.port.in.RegisterClickLogUseCase;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.ProductClickLog;
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
class RegisterClickLogService implements RegisterClickLogUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public void register(ConsumerRecords<String, String> records) {
        List<ProductClickLog> logs = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {
            String payload = record.value();
            ProductClickLog clickLog = parseJson(payload, ProductClickLog.class);
            if (ObjectUtils.isEmpty(clickLog)) {
                log.error("[registerClickLogService] parsingFailed - {} ", payload);
                continue;
            }
            logs.add(clickLog);
        }

        if (!logs.isEmpty()) {
            logStoragePort.register(logs, CollectionName.CLICK());
        }
    }
}
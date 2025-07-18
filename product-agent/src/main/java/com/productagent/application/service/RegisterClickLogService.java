package com.productagent.application.service;

import com.productagent.application.port.in.RegisterClickLogUseCase;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.Product;
import com.productagent.domain.model.ProductClickLog;
import com.productagent.infrastructure.constant.CollectionName;
import com.productagent.infrastructure.util.JsonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Map<Long, Product> productMap = new HashMap<>();

        for (ConsumerRecord<String, String> record : records) {
            String payload = record.value();
            ProductClickLog clickLog = JsonUtil.parseJson(payload, ProductClickLog.class);
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
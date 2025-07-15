package com.productagent.application.service.register_click_log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productagent.application.port.in.RegisterClickLogUseCase;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.application.port.out.ProductStoragePort;
import com.productagent.domain.model.Product;
import com.productagent.domain.model.ProductClickLog;
import com.productagent.infrastructure.exception.CustomNotFoundException;
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

@Slf4j
@Service
@RequiredArgsConstructor
class RegisterClickLogService implements RegisterClickLogUseCase {

    private final LogStoragePort logStoragePort;
    private final ProductStoragePort productStoragePort;

    @Override
    public void register(ConsumerRecords<String, String> records) {
        List<ProductClickLog> logs = new ArrayList<>();
        Map<Long, Product> productMap = new HashMap<>();

        for (ConsumerRecord<String, String> record : records) {
            String payload = record.value();
            try {
                ProductClickLog log = JsonUtil.parseJson(payload, ProductClickLog.class);
                logs.add(log);

                Product metric = productMap.getOrDefault(log.productId(),
                    productStoragePort.findByIdAndDeleteYn(log.productId(), "N"));
                metric.updateHitCount();
                productMap.put(log.productId(), metric);

            } catch (JsonProcessingException e) {
                log.error("[registerClickLogService] parsingFailed - {} ", payload);
            } catch (CustomNotFoundException e) {
                log.error("[registerClickLogService] unknownProduct - {} ", payload);
            }
        }

        if (!logs.isEmpty()) {
            logStoragePort.registerClickLogs(logs);
            productStoragePort.registerMetrics(productMap.values().stream().toList());
        }
    }
}
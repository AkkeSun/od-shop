package com.productagent.application.service;

import static com.common.infrastructure.util.JsonUtil.parseJson;

import com.productagent.application.port.in.RegisterSearchLogUseCase;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.SearchLog;
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
class RegisterSearchLogService implements RegisterSearchLogUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public void register(ConsumerRecords<String, String> records) {
        List<SearchLog> logs = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            String payload = record.value();
            SearchLog searchLog = parseJson(payload, SearchLog.class);

            if (ObjectUtils.isEmpty(searchLog)) {
                log.error("[registerSearchLog] parsingFailed - {} ", payload);
                continue;
            }
            logs.add(parseJson(payload, SearchLog.class));
        }
        if (!logs.isEmpty()) {
            logStoragePort.register(logs, CollectionName.SEARCH());
        }
    }
}

package com.productagent.adapter.out.persistence.mongo;

import static com.productagent.infrastructure.util.DateUtil.getCurrentDate;

import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.DlqLog;
import com.productagent.domain.model.ProductHistory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LogStorageAdapter implements LogStoragePort {

    private final MongoTemplate mongoTemplate;

    @Override
    public void registerHistories(List<ProductHistory> histories) {
        List<ProductHistoryDocument> documents = histories.stream()
            .map(ProductHistoryDocument::of)
            .toList();
        mongoTemplate.insert(documents, "history_" + getCurrentDate());
    }

    @Override
    public void registerDlqLog(DlqLog log) {
        mongoTemplate.insert(DlqLogDocument.of(log), "dql_" + getCurrentDate());
    }
}

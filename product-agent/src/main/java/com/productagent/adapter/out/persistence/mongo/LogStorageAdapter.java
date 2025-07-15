package com.productagent.adapter.out.persistence.mongo;

import static com.productagent.infrastructure.util.DateUtil.getCurrentDate;

import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.DlqLog;
import com.productagent.domain.model.ProductClickLog;
import com.productagent.domain.model.ProductHistory;
import com.productagent.domain.model.SearchLog;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LogStorageAdapter implements LogStoragePort {

    private final MongoTemplate mongoTemplate;

    @Override
    public void registerHistories(List<ProductHistory> logs) {
        List<ProductHistoryDocument> documents = logs.stream()
            .map(ProductHistoryDocument::of)
            .toList();
        mongoTemplate.insert(documents, "history_" + getCurrentDate());
    }

    @Override
    public void registerClickLogs(List<ProductClickLog> logs) {
        List<ProductClickLogDocument> documents = logs.stream()
            .map(ProductClickLogDocument::of)
            .toList();
        mongoTemplate.insert(documents, "click_" + getCurrentDate());
    }

    @Override
    public void registerSearchLogs(List<SearchLog> logs) {
        List<SearchLogDocument> documents = logs.stream()
            .map(SearchLogDocument::of)
            .toList();
        mongoTemplate.insert(documents, "search_" + getCurrentDate());
    }

    @Override
    public void registerDlqLog(DlqLog log) {
        mongoTemplate.insert(DlqLogDocument.of(log), "dql_" + getCurrentDate());
    }

    @Override
    public void registerDlqLogs(List<DlqLog> logs) {
        mongoTemplate.insert(logs.stream().map(DlqLogDocument::of).toList(),
            "dql_" + getCurrentDate());
    }
}

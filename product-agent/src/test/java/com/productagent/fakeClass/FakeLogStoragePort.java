package com.productagent.fakeClass;

import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.ProductClickLog;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeLogStoragePort implements LogStoragePort {

    public Map<String, List<Object>> database = new HashMap<>();
    public boolean shouldThrowException = false;
    public LocalDateTime lastMetricUpdateTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

    @Override
    public void register(Object document, String collectionName) {
        if (shouldThrowException) {
            throw new RuntimeException("Simulated exception");
        }

        if (document instanceof List<?>) {
            database.computeIfAbsent(collectionName, k -> new ArrayList<>())
                .addAll((List<?>) document);
            log.info("FakeLogStoragePort registered: collection={}, count={}", collectionName,
                ((List<?>) document).size());
        } else {
            database.computeIfAbsent(collectionName, k -> new ArrayList<>()).add(document);
            log.info("FakeLogStoragePort registered: collection={}, document={}", collectionName,
                document.getClass().getSimpleName());
        }
    }

    @Override
    public LocalDateTime findLastMetricUpdateTime() {
        return lastMetricUpdateTime;
    }

    @Override
    public List<ProductClickLog> findClickLogBetween(LocalDateTime start, LocalDateTime end) {
        return database.values().stream()
            .flatMap(List::stream)
            .filter(obj -> obj instanceof ProductClickLog)
            .map(obj -> (ProductClickLog) obj)
            .filter(log -> {
                LocalDateTime logTime = LocalDateTime.parse(log.regDateTime());
                return (logTime.isEqual(start) || logTime.isAfter(start))
                    && (logTime.isBefore(end) || logTime.isEqual(end));
            })
            .collect(Collectors.toList());
    }

    @Override
    public void dropCollection(LocalDateTime targetDate) {
        String targetKey = database.keySet().stream()
            .filter(key -> key.contains(targetDate.toString()))
            .findFirst()
            .orElse(null);

        if (targetKey != null) {
            database.remove(targetKey);
            log.info("FakeLogStoragePort dropped collection: {}", targetKey);
        }
    }
}

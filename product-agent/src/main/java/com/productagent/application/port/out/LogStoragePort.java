package com.productagent.application.port.out;

import com.productagent.domain.model.ProductClickLog;
import java.time.LocalDateTime;
import java.util.List;

public interface LogStoragePort {

    void register(Object document, String collectionName);

    LocalDateTime findLastMetricUpdateTime();

    List<ProductClickLog> findClickLogBetween(LocalDateTime start, LocalDateTime end);

    void dropCollection(LocalDateTime targetDate);
}

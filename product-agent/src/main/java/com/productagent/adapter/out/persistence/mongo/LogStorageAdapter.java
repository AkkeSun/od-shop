package com.productagent.adapter.out.persistence.mongo;

import static com.common.infrastructure.util.DateUtil.formatDate;
import static com.common.infrastructure.util.DateUtil.parseDateTime;
import static com.productagent.infrastructure.constant.CollectionName.METRIC_UPDATE_TIME;

import com.common.infrastructure.util.DateUtil;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.MetricUpdateTime;
import com.productagent.domain.model.ProductClickLog;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LogStorageAdapter implements LogStoragePort {

    private final MongoTemplate mongoTemplate;

    @Override
    public void register(Object document, String collectionName) {
        if (document instanceof Collection) {
            mongoTemplate.insert((Collection<?>) document, collectionName);
        } else {
            mongoTemplate.insert(document, collectionName);
        }
    }

    @Override
    public LocalDateTime findLastMetricUpdateTime() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "regDateTime")).limit(1);
        MetricUpdateTime metricUpdateTime = mongoTemplate.findOne(query, MetricUpdateTime.class,
            METRIC_UPDATE_TIME);

        if (metricUpdateTime == null) {
            return LocalDateTime.now().toLocalDate().atStartOfDay();
        }
        return parseDateTime(metricUpdateTime.regDateTime());
    }

    @Override
    public List<ProductClickLog> findClickLogBetween(LocalDateTime start, LocalDateTime end) {
        Query query = new Query();
        query.addCriteria(Criteria.where("regDateTime")
            .gte(DateUtil.formatDateTime(start))
            .lte(DateUtil.formatDateTime(end)));
        return mongoTemplate.find(query, ProductClickLog.class, "click_" + formatDate(end));
    }

    @Override
    public void dropCollection(LocalDateTime targetDate) {
        String date = formatDate(targetDate);
        mongoTemplate.dropCollection("history_" + date);
        mongoTemplate.dropCollection("click_" + date);
        mongoTemplate.dropCollection("search_" + date);
        mongoTemplate.dropCollection("dql_" + date);
    }
}

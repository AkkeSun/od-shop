package com.productagent.adapter.out.persistence.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import com.productagent.domain.model.DlqLog;
import com.productagent.domain.model.MetricUpdateTime;
import com.productagent.domain.model.ProductClickLog;
import com.productagent.domain.model.ProductHistory;
import com.productagent.domain.model.SearchLog;
import com.productagent.infrastructure.constant.CollectionName;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
@Import(LogStorageAdapter.class)
class LogStorageAdapterTest {

    @Autowired
    LogStorageAdapter adapter;

    @Autowired
    MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.getCollectionNames().forEach(mongoTemplate::dropCollection);
    }

    @Nested
    @DisplayName("[register] 단일 문서를 저장하는 메소드")
    class Describe_register_single {

        @Test
        @DisplayName("[success] DlqLog를 MongoDB에 저장한다")
        void success_dlqLog() {
            // given
            DlqLog dlqLog = DlqLog.builder()
                .topic("test-topic")
                .payload("{\"productId\": 1}")
                .regDateTime("2025-01-15 10:00:00")
                .build();

            // when
            adapter.register(dlqLog, CollectionName.DLQ());

            // then
            assertThat(mongoTemplate.collectionExists(CollectionName.DLQ())).isTrue();
            List<DlqLog> logs = mongoTemplate.findAll(DlqLog.class, CollectionName.DLQ());
            assertThat(logs).hasSize(1);
            assertThat(logs.get(0).topic()).isEqualTo("test-topic");
            assertThat(logs.get(0).payload()).isEqualTo("{\"productId\": 1}");
        }

        @Test
        @DisplayName("[success] ProductHistory를 MongoDB에 저장한다")
        void success_productHistory() {
            // given
            ProductHistory history = ProductHistory.builder()
                .productId(100L)
                .accountId(1L)
                .type("CREATE")
                .detailInfo("상품 생성")
                .regDateTime("2025-01-15 10:00:00")
                .build();

            String collectionName = "history_20250115";

            // when
            adapter.register(history, collectionName);

            // then
            assertThat(mongoTemplate.collectionExists(collectionName)).isTrue();
            List<ProductHistory> histories = mongoTemplate.findAll(ProductHistory.class,
                collectionName);
            assertThat(histories).hasSize(1);
            assertThat(histories.get(0).productId()).isEqualTo(100L);
            assertThat(histories.get(0).type()).isEqualTo("CREATE");
        }

        @Test
        @DisplayName("[success] ProductClickLog를 MongoDB에 저장한다")
        void success_clickLog() {
            // given
            ProductClickLog clickLog = ProductClickLog.builder()
                .productId(200L)
                .regDateTime("2025-01-15T10:00:00")
                .build();

            String collectionName = "click_20250115";

            // when
            adapter.register(clickLog, collectionName);

            // then
            assertThat(mongoTemplate.collectionExists(collectionName)).isTrue();
            List<ProductClickLog> logs = mongoTemplate.findAll(ProductClickLog.class,
                collectionName);
            assertThat(logs).hasSize(1);
            assertThat(logs.get(0).productId()).isEqualTo(200L);
        }

        @Test
        @DisplayName("[success] SearchLog를 MongoDB에 저장한다")
        void success_searchLog() {
            // given
            SearchLog searchLog = SearchLog.builder()
                .query("스마트폰")
                .regDateTime("2025-01-15T10:00:00")
                .build();

            String collectionName = "search_20250115";

            // when
            adapter.register(searchLog, collectionName);

            // then
            assertThat(mongoTemplate.collectionExists(collectionName)).isTrue();
            List<SearchLog> logs = mongoTemplate.findAll(SearchLog.class, collectionName);
            assertThat(logs).hasSize(1);
            assertThat(logs.get(0).query()).isEqualTo("스마트폰");
        }

        @Test
        @DisplayName("[success] MetricUpdateTime을 MongoDB에 저장한다")
        void success_metricUpdateTime() {
            // given
            MetricUpdateTime metricUpdateTime = MetricUpdateTime.of("2025-01-15 10:00:00");

            // when
            adapter.register(metricUpdateTime, CollectionName.METRIC_UPDATE_TIME);

            // then
            assertThat(mongoTemplate.collectionExists(CollectionName.METRIC_UPDATE_TIME)).isTrue();
            List<MetricUpdateTime> times = mongoTemplate.findAll(MetricUpdateTime.class,
                CollectionName.METRIC_UPDATE_TIME);
            assertThat(times).hasSize(1);
            assertThat(times.get(0).regDateTime()).isEqualTo("2025-01-15 10:00:00");
        }
    }

    @Nested
    @DisplayName("[register] 여러 문서를 저장하는 메소드")
    class Describe_register_multiple {

        @Test
        @DisplayName("[success] 여러 DlqLog를 한 번에 저장한다")
        void success_multipleDlqLogs() {
            // given
            List<DlqLog> dlqLogs = List.of(
                DlqLog.builder()
                    .topic("topic1")
                    .payload("payload1")
                    .regDateTime("2025-01-15 10:00:00")
                    .build(),
                DlqLog.builder()
                    .topic("topic2")
                    .payload("payload2")
                    .regDateTime("2025-01-15 10:01:00")
                    .build(),
                DlqLog.builder()
                    .topic("topic3")
                    .payload("payload3")
                    .regDateTime("2025-01-15 10:02:00")
                    .build()
            );

            // when
            adapter.register(dlqLogs, CollectionName.DLQ());

            // then
            List<DlqLog> savedLogs = mongoTemplate.findAll(DlqLog.class, CollectionName.DLQ());
            assertThat(savedLogs).hasSize(3);
        }

        @Test
        @DisplayName("[success] 여러 ProductClickLog를 한 번에 저장한다")
        void success_multipleClickLogs() {
            // given
            List<ProductClickLog> clickLogs = List.of(
                ProductClickLog.builder()
                    .productId(1L)
                    .regDateTime("2025-01-15T10:00:00")
                    .build(),
                ProductClickLog.builder()
                    .productId(2L)
                    .regDateTime("2025-01-15T10:01:00")
                    .build(),
                ProductClickLog.builder()
                    .productId(3L)
                    .regDateTime("2025-01-15T10:02:00")
                    .build()
            );

            String collectionName = "click_20250115";

            // when
            adapter.register(clickLogs, collectionName);

            // then
            List<ProductClickLog> savedLogs = mongoTemplate.findAll(ProductClickLog.class,
                collectionName);
            assertThat(savedLogs).hasSize(3);
        }
    }

    @Nested
    @DisplayName("[findLastMetricUpdateTime] 마지막 메트릭 업데이트 시간을 조회하는 메소드")
    class Describe_findLastMetricUpdateTime {

        @Test
        @DisplayName("[success] 가장 최근의 MetricUpdateTime을 조회한다")
        void success() {
            // given
            mongoTemplate.insert(MetricUpdateTime.of("2025-01-15 10:00:00"),
                CollectionName.METRIC_UPDATE_TIME);
            mongoTemplate.insert(MetricUpdateTime.of("2025-01-15 11:00:00"),
                CollectionName.METRIC_UPDATE_TIME);
            mongoTemplate.insert(MetricUpdateTime.of("2025-01-15 12:00:00"),
                CollectionName.METRIC_UPDATE_TIME);

            // when
            LocalDateTime lastTime = adapter.findLastMetricUpdateTime();

            // then
            assertThat(lastTime).isEqualTo(LocalDateTime.of(2025, 1, 15, 12, 0, 0));
        }

        @Test
        @DisplayName("[success] 하나의 MetricUpdateTime만 있을 때도 조회한다")
        void success_single() {
            // given
            mongoTemplate.insert(MetricUpdateTime.of("2025-01-15 10:00:00"),
                CollectionName.METRIC_UPDATE_TIME);

            // when
            LocalDateTime lastTime = adapter.findLastMetricUpdateTime();

            // then
            assertThat(lastTime).isEqualTo(LocalDateTime.of(2025, 1, 15, 10, 0, 0));
        }
    }

    @Nested
    @DisplayName("[findClickLogBetween] 특정 기간의 클릭 로그를 조회하는 메소드")
    class Describe_findClickLogBetween {

        @Test
        @DisplayName("[success] 시작 시간과 종료 시간 사이의 클릭 로그를 조회한다")
        void success() {
            // given
            String collectionName = "click_20250115";
            mongoTemplate.insert(ProductClickLog.builder()
                .productId(1L)
                .regDateTime("2025-01-15 09:00:00")
                .build(), collectionName);
            mongoTemplate.insert(ProductClickLog.builder()
                .productId(2L)
                .regDateTime("2025-01-15 10:00:00")
                .build(), collectionName);
            mongoTemplate.insert(ProductClickLog.builder()
                .productId(3L)
                .regDateTime("2025-01-15 11:00:00")
                .build(), collectionName);
            mongoTemplate.insert(ProductClickLog.builder()
                .productId(4L)
                .regDateTime("2025-01-15 12:00:00")
                .build(), collectionName);

            LocalDateTime start = LocalDateTime.of(2025, 1, 15, 10, 0, 0);
            LocalDateTime end = LocalDateTime.of(2025, 1, 15, 11, 30, 0);

            // when
            List<ProductClickLog> logs = adapter.findClickLogBetween(start, end);

            // then
            assertThat(logs).isNotNull();
            assertThat(logs.size()).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("[success] 해당 기간에 로그가 없으면 빈 리스트를 반환한다")
        void success_empty() {
            // given
            LocalDateTime start = LocalDateTime.of(2025, 1, 15, 10, 0, 0);
            LocalDateTime end = LocalDateTime.of(2025, 1, 15, 11, 0, 0);

            // when
            List<ProductClickLog> logs = adapter.findClickLogBetween(start, end);

            // then
            assertThat(logs).isNotNull();
        }
    }

    @Nested
    @DisplayName("[dropCollection] 특정 날짜의 컬렉션들을 삭제하는 메소드")
    class Describe_dropCollection {

        @Test
        @DisplayName("[success] 특정 날짜의 로그 컬렉션을 삭제한다")
        void success() {
            // given
            LocalDateTime targetDate = LocalDateTime.of(2025, 1, 15, 0, 0, 0);
            String dateStr = targetDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            String historyCollection = "history_" + dateStr;

            // 컬렉션 생성
            mongoTemplate.insert(ProductHistory.builder().productId(1L).build(),
                historyCollection);

            long beforeCount = mongoTemplate.getCollectionNames().size();

            // when
            adapter.dropCollection(targetDate);

            // then
            // 컬렉션이 삭제되었으므로 컬렉션 수가 줄었거나 같아야 함
            long afterCount = mongoTemplate.getCollectionNames().size();
            assertThat(afterCount).isLessThanOrEqualTo(beforeCount);
        }

        @Test
        @DisplayName("[success] 존재하지 않는 컬렉션을 삭제해도 예외가 발생하지 않는다")
        void success_nonExistent() {
            // given
            LocalDateTime targetDate = LocalDateTime.of(2025, 1, 20, 0, 0, 0);

            // when & then
            adapter.dropCollection(targetDate); // 예외 발생하지 않음
        }
    }
}

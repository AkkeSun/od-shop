package com.productagent.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.productagent.domain.model.ProductClickLog;
import com.productagent.fakeClass.FakeLogStoragePort;
import com.productagent.infrastructure.constant.CollectionName;
import java.util.ArrayList;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class RegisterClickLogServiceTest {

    RegisterClickLogService service;
    FakeLogStoragePort fakeLogStoragePort;

    RegisterClickLogServiceTest() {
        fakeLogStoragePort = new FakeLogStoragePort();
        service = new RegisterClickLogService(fakeLogStoragePort);
    }

    @BeforeEach
    void setup() {
        fakeLogStoragePort.database.clear();
        fakeLogStoragePort.shouldThrowException = false;
    }

    @Nested
    @DisplayName("[register] 클릭 로그를 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 여러 클릭 로그를 저장한다")
        void success(CapturedOutput output) {
            // given
            List<ConsumerRecord<String, String>> recordList = new ArrayList<>();
            recordList.add(new ConsumerRecord<>("click-topic", 0, 0L, "key1",
                "{\"productId\": 1, \"regDateTime\": \"2025-01-15T10:00:00\"}"));
            recordList.add(new ConsumerRecord<>("click-topic", 0, 1L, "key2",
                "{\"productId\": 2, \"regDateTime\": \"2025-01-15T10:00:01\"}"));
            recordList.add(new ConsumerRecord<>("click-topic", 0, 2L, "key3",
                "{\"productId\": 3, \"regDateTime\": \"2025-01-15T10:00:02\"}"));

            ConsumerRecords<String, String> records = new ConsumerRecords<>(
                java.util.Map.of(
                    new TopicPartition("click-topic", 0), recordList
                )
            );

            // when
            service.register(records);

            // then
            List<Object> clickLogs = fakeLogStoragePort.database.get(CollectionName.CLICK());
            assertThat(clickLogs).hasSize(3);
            assertThat(clickLogs).allMatch(log -> log instanceof ProductClickLog);
            assertThat(output.toString()).contains("FakeLogStoragePort registered");
        }

        @Test
        @DisplayName("[success] 빈 레코드를 처리한다")
        void success_emptyRecords() {
            // given
            ConsumerRecords<String, String> emptyRecords = new ConsumerRecords<>(
                java.util.Map.of()
            );

            // when
            service.register(emptyRecords);

            // then
            List<Object> clickLogs = fakeLogStoragePort.database.get(CollectionName.CLICK());
            assertThat(clickLogs).isNull();
        }

        @Test
        @DisplayName("[success] null이 포함된 결과는 건너뛰고 유효한 것만 저장한다")
        void success_skipNullResults(CapturedOutput output) {
            // given
            List<ConsumerRecord<String, String>> recordList = new ArrayList<>();
            recordList.add(new ConsumerRecord<>("click-topic", 0, 0L, "key1",
                "{\"productId\": 1, \"regDateTime\": \"2025-01-15T10:00:00\"}"));
            recordList.add(new ConsumerRecord<>("click-topic", 0, 2L, "key3",
                "{\"productId\": 3, \"regDateTime\": \"2025-01-15T10:00:02\"}"));

            ConsumerRecords<String, String> records = new ConsumerRecords<>(
                java.util.Map.of(
                    new TopicPartition("click-topic", 0), recordList
                )
            );

            // when
            service.register(records);

            // then
            List<Object> clickLogs = fakeLogStoragePort.database.get(CollectionName.CLICK());
            assertThat(clickLogs).hasSize(2);
        }

        @Test
        @DisplayName("[success] 여러 파티션의 레코드를 처리한다")
        void success_multiplePartitions(CapturedOutput output) {
            // given
            List<ConsumerRecord<String, String>> recordList = new ArrayList<>();
            recordList.add(new ConsumerRecord<>("click-topic", 0, 0L, "key1",
                "{\"productId\": 1, \"regDateTime\": \"2025-01-15T10:00:00\"}"));
            recordList.add(new ConsumerRecord<>("click-topic", 0, 1L, "key2",
                "{\"productId\": 2, \"regDateTime\": \"2025-01-15T10:00:01\"}"));

            ConsumerRecords<String, String> records = new ConsumerRecords<>(
                java.util.Map.of(
                    new TopicPartition("click-topic", 0), recordList
                )
            );

            // when
            service.register(records);

            // then
            List<Object> clickLogs = fakeLogStoragePort.database.get(CollectionName.CLICK());
            assertThat(clickLogs).isNotNull();
        }
    }
}

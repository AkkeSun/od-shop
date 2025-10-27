package com.productagent.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.productagent.application.port.in.command.RegisterDlqCommand;
import com.productagent.domain.model.DlqLog;
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
class RegisterDlqServiceTest {

    RegisterDlqService service;
    FakeLogStoragePort fakeLogStoragePort;

    RegisterDlqServiceTest() {
        fakeLogStoragePort = new FakeLogStoragePort();
        service = new RegisterDlqService(fakeLogStoragePort);
    }

    @BeforeEach
    void setup() {
        fakeLogStoragePort.database.clear();
        fakeLogStoragePort.shouldThrowException = false;
    }

    @Nested
    @DisplayName("[register] Command로 DLQ를 등록하는 메소드")
    class Describe_register_command {

        @Test
        @DisplayName("[success] DLQ 로그를 저장한다")
        void success(CapturedOutput output) {
            // given
            RegisterDlqCommand command = RegisterDlqCommand.builder()
                .topic("test-topic")
                .payload("{\"productId\": 1}")
                .build();

            // when
            service.register(command);

            // then
            assertThat(fakeLogStoragePort.database.get(CollectionName.DLQ())).hasSize(1);
            assertThat(output.toString()).contains("FakeLogStoragePort registered");
        }

        @Test
        @DisplayName("[error] Storage 장애 시 예외 전파")
        void error_storageFailure() {
            // given
            fakeLogStoragePort.shouldThrowException = true;
            RegisterDlqCommand command = RegisterDlqCommand.builder()
                .topic("test-topic")
                .payload("{\"productId\": 1}")
                .build();

            // when & then
            assertThrows(RuntimeException.class, () -> service.register(command));
        }
    }

    @Nested
    @DisplayName("[register] ConsumerRecords로 DLQ를 등록하는 메소드")
    class Describe_register_records {

        @Test
        @DisplayName("[success] 여러 레코드를 DLQ로 저장한다")
        void success(CapturedOutput output) {
            // given
            List<ConsumerRecord<String, String>> recordList = new ArrayList<>();
            recordList.add(new ConsumerRecord<>("topic1", 0, 0L, "key1", "value1"));
            recordList.add(new ConsumerRecord<>("topic2", 0, 1L, "key2", "value2"));
            recordList.add(new ConsumerRecord<>("topic3", 0, 2L, "key3", "value3"));

            ConsumerRecords<String, String> records = new ConsumerRecords<>(
                java.util.Map.of(
                    new TopicPartition("topic1", 0), recordList
                )
            );

            // when
            service.register(records);

            // then
            assertThat(fakeLogStoragePort.database).isNotEmpty();
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
            if (fakeLogStoragePort.database.containsKey(CollectionName.DLQ())) {
                assertThat(fakeLogStoragePort.database.get(CollectionName.DLQ())).isEmpty();
            }
        }

        @Test
        @DisplayName("[error] Storage 장애 시 예외 전파")
        void error_storageFailure() {
            // given
            fakeLogStoragePort.shouldThrowException = true;
            List<ConsumerRecord<String, String>> recordList = new ArrayList<>();
            recordList.add(new ConsumerRecord<>("topic1", 0, 0L, "key1", "value1"));

            ConsumerRecords<String, String> records = new ConsumerRecords<>(
                java.util.Map.of(
                    new TopicPartition("topic1", 0), recordList
                )
            );

            // when & then
            assertThrows(RuntimeException.class, () -> service.register(records));
        }
    }
}

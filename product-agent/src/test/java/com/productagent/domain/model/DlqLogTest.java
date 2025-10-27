package com.productagent.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DlqLogTest {

    @Nested
    @DisplayName("[of] ConsumerRecord로 DlqLog 객체를 생성하는 정적 팩토리 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] ConsumerRecord로부터 DlqLog를 생성한다")
        void success() {
            // given
            String topic = "product-topic";
            String payload = "{\"productId\": 1, \"name\": \"Test Product\"}";
            ConsumerRecord<String, String> record = new ConsumerRecord<>(
                topic,
                0,
                0L,
                "key",
                payload
            );

            // when
            DlqLog dlqLog = DlqLog.of(record);

            // then
            assertThat(dlqLog.topic()).isEqualTo(topic);
            assertThat(dlqLog.payload()).isEqualTo(payload);
            assertThat(dlqLog.regDateTime()).isNotNull();
        }

        @Test
        @DisplayName("[success] 다른 토픽의 ConsumerRecord로부터 DlqLog를 생성한다")
        void success_differentTopic() {
            // given
            String topic = "order-topic";
            String payload = "{\"orderId\": 123, \"status\": \"FAILED\"}";
            ConsumerRecord<String, String> record = new ConsumerRecord<>(
                topic,
                0,
                0L,
                "key",
                payload
            );

            // when
            DlqLog dlqLog = DlqLog.of(record);

            // then
            assertThat(dlqLog.topic()).isEqualTo(topic);
            assertThat(dlqLog.payload()).isEqualTo(payload);
            assertThat(dlqLog.regDateTime()).isNotNull();
        }

        @Test
        @DisplayName("[success] 긴 payload를 가진 ConsumerRecord로부터 DlqLog를 생성한다")
        void success_longPayload() {
            // given
            String topic = "review-topic";
            String payload = "{\"reviewId\": 1, \"productId\": 100, \"customerId\": 200, "
                + "\"score\": 4.5, \"content\": \"This is a very long review content with many "
                + "characters to test the payload handling in DlqLog creation.\"}";
            ConsumerRecord<String, String> record = new ConsumerRecord<>(
                topic,
                0,
                0L,
                "key",
                payload
            );

            // when
            DlqLog dlqLog = DlqLog.of(record);

            // then
            assertThat(dlqLog.topic()).isEqualTo(topic);
            assertThat(dlqLog.payload()).isEqualTo(payload);
            assertThat(dlqLog.regDateTime()).isNotNull();
        }

        @Test
        @DisplayName("[success] 빈 payload를 가진 ConsumerRecord로부터 DlqLog를 생성한다")
        void success_emptyPayload() {
            // given
            String topic = "empty-topic";
            String payload = "";
            ConsumerRecord<String, String> record = new ConsumerRecord<>(
                topic,
                0,
                0L,
                "key",
                payload
            );

            // when
            DlqLog dlqLog = DlqLog.of(record);

            // then
            assertThat(dlqLog.topic()).isEqualTo(topic);
            assertThat(dlqLog.payload()).isEmpty();
            assertThat(dlqLog.regDateTime()).isNotNull();
        }

        @Test
        @DisplayName("[success] 여러 파티션과 오프셋을 가진 ConsumerRecord로부터 DlqLog를 생성한다")
        void success_differentPartitionAndOffset() {
            // given
            String topic = "multi-partition-topic";
            String payload = "{\"data\": \"test\"}";
            ConsumerRecord<String, String> record = new ConsumerRecord<>(
                topic,
                5, // partition
                12345L, // offset
                "key",
                payload
            );

            // when
            DlqLog dlqLog = DlqLog.of(record);

            // then
            assertThat(dlqLog.topic()).isEqualTo(topic);
            assertThat(dlqLog.payload()).isEqualTo(payload);
            assertThat(dlqLog.regDateTime()).isNotNull();
        }
    }
}

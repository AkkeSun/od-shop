package com.account.adapter.out.producer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.account.IntegrationTestSupport;
import com.account.applicaiton.port.out.CachePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

class AccountProducerAdapterTest extends IntegrationTestSupport {

    @Autowired
    AccountProducerAdapter adapter;
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    CachePort cachePort;

    @Nested
    @DisplayName("[send] 카프카로 메시지를 전송하는 메소드")
    class Describe_send {

        @Test
        @DisplayName("[success] 메시지를 성공적으로 전송하는 경우 성공 로그가 기록되는지 확인한다.")
        void success(CapturedOutput output) throws InterruptedException {
            // given
            String topic = "test-topic";
            String message = "test-message";

            // when
            adapter.sendMessage(topic, message);
            Thread.sleep(1000);

            // then
            assert output.toString().contains("[test-topic] ==> test-message");
        }

        @Test
        @DisplayName("[error] 메시지 전송중 오류 발생시 sendMessageFallback 메서드가 실행되고 ")
        void error(CapturedOutput output) throws InterruptedException {
            // given
            KafkaTemplate<String, String> mockKafkaTemplate = mock(KafkaTemplate.class);
            ReflectionTestUtils.setField(adapter, "kafkaTemplate", mockKafkaTemplate);
            when(mockKafkaTemplate.send(anyString(), anyString()))
                .thenThrow(new RuntimeException("Kafka send error"));
            String topic = "test-topic";
            String message = "test-message";

            // when
            adapter.sendMessage(topic, message);
            Thread.sleep(1000);
            List<String> keys = scanByPattern("test-topic*");
            String s = redisTemplate.opsForValue().get(keys.getFirst());

            // then
            assert output.toString().contains("kafka sendMessage Error : test-topic");
            assert Objects.equals(s, message);
        }
    }

    private List<String> scanByPattern(String pattern) {
        RedisConnection redisConnection =
            redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions =
            ScanOptions.scanOptions().count(50).match(pattern).build();
        Cursor cursor = redisConnection.scan(scanOptions);

        List<String> redisKeys = new ArrayList<>();
        while (cursor.hasNext()) {
            String key = new String((byte[]) cursor.next());
            redisKeys.add(key);
        }

        return redisKeys;
    }
}
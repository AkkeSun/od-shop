package com.order.adapter.out.persistence.redis;

import static org.assertj.core.api.Assertions.assertThat;

import com.order.IntegrationTestSupport;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class RedisStorageAdapterTest extends IntegrationTestSupport {

    @Autowired
    RedisStorageAdapter adapter;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setup() {
        circuitBreakerRegistry.circuitBreaker("redis").reset();
    }

    @AfterEach
    void tearDown() {
        // Redis 데이터 정리
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
    }

    @Nested
    @DisplayName("[findData] 단일 데이터를 조회하는 메소드")
    class Describe_findData {

        @Test
        @DisplayName("[success] Redis에서 데이터를 조회한다")
        void success() {
            // given
            String key = "test:order:1";
            TestData testData = TestData.builder()
                .id(1L)
                .name("Test Order")
                .build();

            redisTemplate.opsForValue().set(key, JsonUtil.toJsonString(testData));

            // when
            TestData result = adapter.findData(key, TestData.class);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Test Order");
        }

        @Test
        @DisplayName("[success] 데이터가 없으면 null을 반환한다")
        void success_notFound() {
            // given
            String key = "test:order:999";

            // when
            TestData result = adapter.findData(key, TestData.class);

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("[findDataList] 리스트 데이터를 조회하는 메소드")
    class Describe_findDataList {

        @Test
        @DisplayName("[success] Redis에서 리스트 데이터를 조회한다")
        void success() {
            // given
            String key = "test:orders";
            List<TestData> testDataList = List.of(
                TestData.builder().id(1L).name("Order 1").build(),
                TestData.builder().id(2L).name("Order 2").build()
            );

            redisTemplate.opsForValue().set(key, JsonUtil.toJsonString(testDataList));

            // when
            List<TestData> result = adapter.findDataList(key, TestData.class);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(1).getId()).isEqualTo(2L);
        }

        @Test
        @DisplayName("[success] 데이터가 없으면 빈 리스트를 반환한다")
        void success_emptyList() {
            // given
            String key = "test:orders:empty";

            // when
            List<TestData> result = adapter.findDataList(key, TestData.class);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("[register] 데이터를 저장하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] Redis에 데이터를 저장한다")
        void success() {
            // given
            String key = "test:order:register";
            TestData testData = TestData.builder()
                .id(1L)
                .name("New Order")
                .build();
            String data = JsonUtil.toJsonString(testData);
            long ttl = 60L;

            // when
            adapter.register(key, data, ttl);

            // then
            String storedData = redisTemplate.opsForValue().get(key);
            assertThat(storedData).isNotNull();
            assertThat(storedData).contains("New Order");
        }
    }

    @Nested
    @DisplayName("[delete] 데이터를 삭제하는 메소드")
    class Describe_delete {

        @Test
        @DisplayName("[success] Redis에서 데이터를 삭제한다")
        void success() {
            // given
            String key1 = "test:order:delete:1";
            String key2 = "test:order:delete:2";

            redisTemplate.opsForValue().set(key1, "data1");
            redisTemplate.opsForValue().set(key2, "data2");

            List<String> keys = List.of(key1, key2);

            // when
            adapter.delete(keys);

            // then
            String result1 = redisTemplate.opsForValue().get(key1);
            String result2 = redisTemplate.opsForValue().get(key2);

            assertThat(result1).isNull();
            assertThat(result2).isNull();
        }
    }

    @Nested
    @DisplayName("[getKeys] 패턴으로 키 목록을 조회하는 메소드")
    class Describe_getKeys {

        @Test
        @DisplayName("[success] 패턴에 맞는 키 목록을 조회한다")
        void success() {
            // given
            redisTemplate.opsForValue().set("order:1", "data1");
            redisTemplate.opsForValue().set("order:2", "data2");
            redisTemplate.opsForValue().set("product:1", "data3");

            String pattern = "order:*";

            // when
            List<String> keys = adapter.getKeys(pattern);

            // then
            assertThat(keys).hasSize(2);
            assertThat(keys).allMatch(key -> key.startsWith("order:"));
        }

        @Test
        @DisplayName("[success] 매칭되는 키가 없으면 빈 리스트를 반환한다")
        void success_noMatch() {
            // given
            String pattern = "nonexistent:*";

            // when
            List<String> keys = adapter.getKeys(pattern);

            // then
            assertThat(keys).isEmpty();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestData {
        private Long id;
        private String name;
    }
}

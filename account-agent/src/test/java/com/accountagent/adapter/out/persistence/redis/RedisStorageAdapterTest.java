package com.accountagent.adapter.out.persistence.redis;

import static org.assertj.core.api.Assertions.assertThat;

import com.accountagent.IntegrationTestSupport;
import com.accountagent.domain.model.AccountHistory;
import com.common.infrastructure.util.JsonUtil;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
    }

    @Nested
    @DisplayName("[findAllAccountHistory] AccountHistory를 조회하는 메소드")
    class Describe_findAllAccountHistory {

        @Test
        @DisplayName("[success] Redis에서 AccountHistory를 조회한다")
        void success() {
            // given
            AccountHistory history = AccountHistory.builder()
                .accountId(1L)
                .type("CREATE")
                .detailInfo("계정 생성")
                .regDateTime("2025-10-25 10:00:00")
                .build();

            String key = "account-history:1";
            redisTemplate.opsForValue().set(key, JsonUtil.toJsonString(history));

            // when
            Map<String, AccountHistory> result = adapter.findAllAccountHistory();

            // then
            assertThat(result).isNotEmpty();
            assertThat(result).hasSize(1);
            assertThat(result).containsKey(key);

            AccountHistory foundHistory = result.get(key);
            assertThat(foundHistory.accountId()).isEqualTo(1L);
            assertThat(foundHistory.type()).isEqualTo("CREATE");
            assertThat(foundHistory.detailInfo()).isEqualTo("계정 생성");
            assertThat(foundHistory.regDateTime()).isEqualTo("2025-10-25 10:00:00");
        }

        @Test
        @DisplayName("[success] 여러 개의 AccountHistory를 조회한다")
        void success_multiple() {
            // given
            AccountHistory history1 = AccountHistory.builder()
                .accountId(1L)
                .type("CREATE")
                .detailInfo("계정 생성")
                .regDateTime("2025-10-25 10:00:00")
                .build();

            AccountHistory history2 = AccountHistory.builder()
                .accountId(2L)
                .type("UPDATE")
                .detailInfo("계정 수정")
                .regDateTime("2025-10-25 11:00:00")
                .build();

            AccountHistory history3 = AccountHistory.builder()
                .accountId(3L)
                .type("DELETE")
                .detailInfo("계정 삭제")
                .regDateTime("2025-10-25 12:00:00")
                .build();

            String key1 = "account-history:1";
            String key2 = "account-history:2";
            String key3 = "account-history:3";

            redisTemplate.opsForValue().set(key1, JsonUtil.toJsonString(history1));
            redisTemplate.opsForValue().set(key2, JsonUtil.toJsonString(history2));
            redisTemplate.opsForValue().set(key3, JsonUtil.toJsonString(history3));

            // when
            Map<String, AccountHistory> result = adapter.findAllAccountHistory();

            // then
            assertThat(result).hasSize(3);
            assertThat(result).containsKeys(key1, key2, key3);

            assertThat(result.get(key1).accountId()).isEqualTo(1L);
            assertThat(result.get(key1).type()).isEqualTo("CREATE");

            assertThat(result.get(key2).accountId()).isEqualTo(2L);
            assertThat(result.get(key2).type()).isEqualTo("UPDATE");

            assertThat(result.get(key3).accountId()).isEqualTo(3L);
            assertThat(result.get(key3).type()).isEqualTo("DELETE");
        }

        @Test
        @DisplayName("[success] 데이터가 없으면 빈 Map을 반환한다")
        void success_empty() {
            // given
            // Redis에 데이터 없음

            // when
            Map<String, AccountHistory> result = adapter.findAllAccountHistory();

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("[success] account-history 패턴과 일치하는 데이터만 조회한다")
        void success_patternMatch() {
            // given
            AccountHistory history = AccountHistory.builder()
                .accountId(1L)
                .type("CREATE")
                .detailInfo("계정 생성")
                .regDateTime("2025-10-25 10:00:00")
                .build();

            // account-history 패턴에 매치되는 키
            String matchingKey = "account-history:1";
            redisTemplate.opsForValue().set(matchingKey, JsonUtil.toJsonString(history));

            // 패턴에 매치되지 않는 키들
            redisTemplate.opsForValue().set("other-key:1", "other data");
            redisTemplate.opsForValue().set("user-info:1", "user data");

            // when
            Map<String, AccountHistory> result = adapter.findAllAccountHistory();

            // then
            assertThat(result).hasSize(1);
            assertThat(result).containsKey(matchingKey);
            assertThat(result).doesNotContainKey("other-key:1");
            assertThat(result).doesNotContainKey("user-info:1");
        }
    }

    @Nested
    @DisplayName("[delete] Redis에서 키를 삭제하는 메소드")
    class Describe_delete {

        @Test
        @DisplayName("[success] Redis에서 키를 삭제한다")
        void success() {
            // given
            String key = "account-history:1";
            AccountHistory history = AccountHistory.builder()
                .accountId(1L)
                .type("CREATE")
                .detailInfo("계정 생성")
                .regDateTime("2025-10-25 10:00:00")
                .build();

            redisTemplate.opsForValue().set(key, JsonUtil.toJsonString(history));
            assertThat(redisTemplate.hasKey(key)).isTrue();

            // when
            adapter.delete(key);

            // then
            assertThat(redisTemplate.hasKey(key)).isFalse();
        }

        @Test
        @DisplayName("[success] 존재하지 않는 키를 삭제해도 예외가 발생하지 않는다")
        void success_nonExistentKey() {
            // given
            String key = "non-existent-key";
            assertThat(redisTemplate.hasKey(key)).isFalse();

            // when & then
            adapter.delete(key); // 예외 발생하지 않음
        }

        @Test
        @DisplayName("[success] 여러 키를 순차적으로 삭제한다")
        void success_multiple() {
            // given
            String key1 = "account-history:1";
            String key2 = "account-history:2";
            String key3 = "account-history:3";

            redisTemplate.opsForValue().set(key1, "data1");
            redisTemplate.opsForValue().set(key2, "data2");
            redisTemplate.opsForValue().set(key3, "data3");

            assertThat(redisTemplate.hasKey(key1)).isTrue();
            assertThat(redisTemplate.hasKey(key2)).isTrue();
            assertThat(redisTemplate.hasKey(key3)).isTrue();

            // when
            adapter.delete(key1);
            adapter.delete(key2);
            adapter.delete(key3);

            // then
            assertThat(redisTemplate.hasKey(key1)).isFalse();
            assertThat(redisTemplate.hasKey(key2)).isFalse();
            assertThat(redisTemplate.hasKey(key3)).isFalse();
        }
    }
}

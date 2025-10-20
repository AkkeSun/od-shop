package com.product.adapter.out.persistence.redis;

import static com.common.infrastructure.util.JsonUtil.parseJson;
import com.common.infrastructure.util.JsonUtil;

import com.product.IntegrationTestSupport;
import com.product.domain.model.Product;
import com.product.domain.model.ProductHistory;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.data.redis.core.RedisTemplate;

class RedisStorageAdapterTest extends IntegrationTestSupport {

    @Autowired
    RedisStorageAdapter adapter;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @AfterEach
    void reset() {
        circuitBreakerRegistry.circuitBreaker("redis").reset();
    }

    @Nested
    @DisplayName("[findData] 레디스에 저장된 오브젝트를 조회하는 메소드")
    class Describe_findData {

        @Test
        @DisplayName("[success] 저장된 데이터가 있다면 데이터를 응답한다.")
        void success() {
            // given
            String key = "findDataTest1";
            Product product = Product.builder().id(10L).build();
            redisTemplate.opsForValue().set(key, JsonUtil.toJsonString(product));

            // when
            Product result = adapter.findData(key, Product.class);

            // then
            assert product.getId().equals(result.getId());

            // clean
            redisTemplate.delete(key);
        }

        @Test
        @DisplayName("[success] 저장된 데이터가 없다면 null 을 응답한다.")
        void success2() {
            // given
            String key = "findDataTest2";

            // when
            Product result = adapter.findData(key, Product.class);

            // then
            assert result == null;
        }

        @Test
        @DisplayName("[success] 작업중 오류가 발생하면 fallback 메소드가 실행된다.")
        void success3(CapturedOutput output) {
            // given
            String key = "findDataTest3";
            Product product = Product.builder().id(10L).build();
            redisTemplate.opsForValue().set(key, JsonUtil.toJsonString(product));

            // when
            ProductHistory result = adapter.findData(key, ProductHistory.class);

            // then
            assert result == null;
            assert output.toString().contains("[findRedisDataFallback] findDataTest3");

            // clean
            redisTemplate.delete(key);
        }
    }

    @Nested
    @DisplayName("[findDataList] 레디스에 저장된 리스트를 조회하는 메소드")
    class Describe_findDataList {

        @Test
        @DisplayName("[success] 저장된 데이터가 있다면 데이터를 응답한다.")
        void success() {
            // given
            String key = "findDataListTest1";
            List<Product> products = List.of(Product.builder().id(10L).build());
            redisTemplate.opsForValue().set(key, JsonUtil.toJsonString(products));

            // when
            List<Product> result = adapter.findDataList(key, Product.class);

            // then
            assert result.getFirst().getId().equals(products.getFirst().getId());

            // clean
            redisTemplate.delete(key);
        }

        @Test
        @DisplayName("[success] 저장된 데이터가 없다면 null 을 응답한다.")
        void success2() {
            // given
            String key = "findDataListTest2";

            // when
            List<Product> result = adapter.findDataList(key, Product.class);

            // then
            assert result.isEmpty();
        }

        @Test
        @DisplayName("[success] 작업중 오류가 발생하면 fallback 메소드가 실행된다.")
        void success3(CapturedOutput output) {
            // given
            String key = "findDataListTest3";
            List<Product> products = List.of(Product.builder().id(10L).build());
            redisTemplate.opsForValue().set(key, JsonUtil.toJsonString(products));

            // when
            List<ProductHistory> result = adapter.findDataList(key, ProductHistory.class);

            // then
            assert result.isEmpty();
            assert output.toString().contains("[findDataListFallback] findDataListTest3");

            // clean
            redisTemplate.delete(key);
        }
    }

    @Nested
    @DisplayName("[register] 레디스에 데이터를 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 데이터를 저장한다.")
        void success() {
            // given
            String key = "registerTest1";
            Product product = Product.builder().id(10L).build();

            // when
            adapter.register(key, JsonUtil.toJsonString(product), 1000L);
            Product result = parseJson(redisTemplate.opsForValue().get(key), Product.class);

            // then
            assert result.getId().equals(product.getId());

            // clean
            redisTemplate.delete(key);
        }
    }
}
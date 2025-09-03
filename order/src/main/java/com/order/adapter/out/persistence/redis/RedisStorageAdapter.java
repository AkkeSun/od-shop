package com.order.adapter.out.persistence.redis;

import static com.order.infrastructure.util.JsonUtil.parseJson;
import static com.order.infrastructure.util.JsonUtil.parseJsonList;

import com.order.applicatoin.port.out.RedisStoragePort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
class RedisStorageAdapter implements RedisStoragePort {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "findDataFallback")
    public <T> T findData(String key, Class<T> clazz) {
        String redisData = redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasText(redisData)) {
            return null;
        }
        return parseJson(redisData, clazz);
    }

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "findDataListFallback")
    public <T> List<T> findDataList(String key, Class<T> clazz) {
        String redisData = redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasText(redisData)) {
            return Collections.emptyList();
        }
        return parseJsonList(redisData, clazz);
    }

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "registerRedis")
    public void register(String key, String data, long ttl) {
        redisTemplate.opsForValue().set(key, data, ttl, TimeUnit.SECONDS);
    }

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "deleteTokenFallback")
    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "getKeysFallback")
    public List<String> getKeys(String input) {
        RedisConnection redisConnection =
            redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions =
            ScanOptions.scanOptions().count(50).match(input).build();
        Cursor cursor = redisConnection.scan(scanOptions);

        List<String> redisKeys = new ArrayList<>();
        while (cursor.hasNext()) {
            String key = new String((byte[]) cursor.next());
            redisKeys.add(key);
        }

        return redisKeys;
    }

    private List<String> getKeysFallback(String input, Throwable throwable) {
        return new ArrayList<>();
    }

    private <T> T findDataFallback(String key, Class<T> clazz,
        Throwable throwable) {
        log.error("[findRedisDataFallback] {}-{}", key, throwable.getMessage());
        return null;
    }

    private <T> List<T> findDataListFallback(String key, Class<T> clazz,
        Throwable throwable) {
        log.error("[findDataListFallback] {}-{}", key, throwable.getMessage());
        return Collections.emptyList();
    }

    private void registerRedis(String key, String data, Throwable throwable) {
        log.error("[registerRedis] {}-{}-{}", key, data, throwable.getMessage());
        System.out.println("check");
    }

    private void deleteTokenFallback(List<String> keys, Throwable throwable) {
    }

}

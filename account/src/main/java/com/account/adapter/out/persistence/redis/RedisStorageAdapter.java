package com.account.adapter.out.persistence.redis;

import static com.account.infrastructure.util.JsonUtil.parseJson;
import static com.account.infrastructure.util.JsonUtil.parseJsonList;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.domain.model.Token;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
class RedisStorageAdapter implements RedisStoragePort {

    private final long validTime;
    private final String tokenCacheKey;
    private final RedisTemplate<String, String> redisTemplate;

    RedisStorageAdapter(RedisTemplate<String, String> redisTemplate,
        @Value("${jwt.token.refresh-valid-time}") long validTime) {
        this.redisTemplate = redisTemplate;
        this.validTime = validTime;
        this.tokenCacheKey = "token::%s-%s";
    }

    @CircuitBreaker(name = "redis", fallbackMethod = "registerTokenFallback")
    public void registerToken(Token token) {
        String key = String.format(tokenCacheKey, token.getEmail(), token.getUserAgent());
        String redisData = toJsonString(token);
        redisTemplate.opsForValue().set(key, redisData, validTime, TimeUnit.MILLISECONDS);
    }

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "findByEmailAndUserAgentFallback")
    public Token findTokenByEmailAndUserAgent(String email, String userAgent) {
        String key = String.format(tokenCacheKey, email, userAgent);
        String redisData = redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasText(redisData)) {
            return null;
        }
        return parseJson(redisData, Token.class);
    }

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

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "deleteByEmailFallback")
    public void deleteTokenByEmail(String email) {
        String key = String.format(tokenCacheKey, email, "*");
        List<String> redisKeys = scanByPattern(key);
        redisTemplate.delete(redisKeys);
    }

    private void registerTokenFallback(Token token, Exception e) {
        log.error("[registerTokenFallback] call - " + e.getMessage());
    }

    private Token findByEmailAndUserAgentFallback(String email, String userAgent, Exception e) {
        log.error("[findByEmailAndUserAgentFallback] call - " + e.getMessage());
        return null;
    }

    private void deleteByEmailFallback(String email, Exception e) {
        log.error("[deleteByEmailFallbackFallback] call - " + e.getMessage());
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

    private List<String> getKeysFallback(String input, Throwable throwable) {
        return new ArrayList<>();
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

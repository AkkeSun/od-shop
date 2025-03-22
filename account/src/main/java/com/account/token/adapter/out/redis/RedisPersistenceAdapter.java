package com.account.token.adapter.out.redis;

import static com.account.global.util.JsonUtil.parseJson;
import static com.account.global.util.JsonUtil.toJsonString;

import com.account.token.application.port.out.FindTokenCachePort;
import com.account.token.application.port.out.RegisterTokenCachePort;
import com.account.token.domain.model.Token;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
class RedisPersistenceAdapter implements RegisterTokenCachePort, FindTokenCachePort {

    private final long validTime;
    private final String tokenCacheKey;
    private final RedisTemplate<String, String> redisTemplate;

    RedisPersistenceAdapter (RedisTemplate<String, String> redisTemplate,
        @Value("${jwt.token.refresh-valid-time}") long validTime) {
        this.redisTemplate = redisTemplate;
        this.validTime = validTime;
        this.tokenCacheKey =  "%s-%s::token";
    }

    @CircuitBreaker(name = "redis", fallbackMethod = "registerTokenFallback")
    public void registerToken(Token token) {
        String key = String.format(tokenCacheKey, token.getEmail(), token.getUserAgent());
        String redisData = toJsonString(token);
        redisTemplate.opsForValue().set(key, redisData, validTime, TimeUnit.MILLISECONDS);
    }

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "findByEmailAndUserAgentFallback")
    public Token findByEmailAndUserAgent(String email, String userAgent) {
        String key = String.format(tokenCacheKey, email, userAgent);
        String redisData = redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasText(redisData)) {
            return null;
        }
        return parseJson(redisData, Token.class);
    }

    private void registerTokenFallback(Token token, Exception e) {
        log.error("[registerTokenFallback] call - " + e.getMessage());
    }

    private Token findByEmailAndUserAgentFallback(String email, String userAgent, Exception e) {
        log.error("[findByEmailAndUserAgentFallback] call - " + e.getMessage());
        return null;
    }
}

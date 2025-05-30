package com.account.adapter.out.cache;

import static com.account.infrastructure.util.JsonUtil.parseJson;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.IntegrationTestSupport;
import com.account.adapter.out.persistence.redis.RedisCacheAdapter;
import com.account.domain.model.Token;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.data.redis.core.RedisTemplate;


class RedisCacheAdapterTest extends IntegrationTestSupport {

    @Autowired
    RedisCacheAdapter adapter;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Nested
    @DisplayName("[registerToken] 토큰 캐시를 등록하는 메소드")
    class Describe_registerToken {

        @Test
        @DisplayName("[success] 토큰 캐시를 정상적으로 등록 하는지 확인한다.")
        void success() {
            // given
            Token token = Token.builder()
                .id(1234L)
                .accountId(11111L)
                .email("email")
                .userAgent("userAgent1")
                .refreshToken("refreshToken")
                .regDateTime("regDateTime")
                .role("role")
                .build();
            String key = "token::email-userAgent1";

            // when
            adapter.registerToken(token);
            Token savedToken = parseJson(redisTemplate.opsForValue().get(key), Token.class);

            // then
            assert savedToken.getId().equals(token.getId());
            assert savedToken.getAccountId().equals(token.getAccountId());
            assert savedToken.getEmail().equals(token.getEmail());
            assert savedToken.getUserAgent().equals(token.getUserAgent());
            assert savedToken.getRefreshToken().equals(token.getRefreshToken());
            assert savedToken.getRegDateTime().equals(token.getRegDateTime());
            assert savedToken.getRole().equals(token.getRole());
        }
    }

    @Nested
    @DisplayName("[findTokenByEmailAndUserAgent] 이메일과 UserAgent로 토큰을 조회하는 메소드")
    class Describe_findTokenByEmailAndUserAgent {

        @Test
        @DisplayName("[success] 조회된 토큰이 있다면 토큰을 응답한다.")
        void success() {
            // given
            Token token = Token.builder()
                .id(1234L)
                .accountId(11111L)
                .email("email")
                .userAgent("userAgent2")
                .refreshToken("refreshToken")
                .regDateTime("regDateTime")
                .role("role")
                .build();
            String key = "token::email-userAgent2";
            redisTemplate.opsForValue().set(key, toJsonString(token));

            // when
            Token savedToken = adapter.findTokenByEmailAndUserAgent("email", "userAgent2");

            // then
            assert savedToken.getId().equals(token.getId());
            assert savedToken.getAccountId().equals(token.getAccountId());
            assert savedToken.getEmail().equals(token.getEmail());
            assert savedToken.getUserAgent().equals(token.getUserAgent());
            assert savedToken.getRefreshToken().equals(token.getRefreshToken());
            assert savedToken.getRegDateTime().equals(token.getRegDateTime());
            assert savedToken.getRole().equals(token.getRole());
        }

        @Test
        @DisplayName("[success] 조회된 토큰이 없다면 null을 응답한다.")
        void success2() {
            // when
            Token savedToken = adapter.findTokenByEmailAndUserAgent("email", "test");

            // then
            assert savedToken == null;
        }

        @Test
        @DisplayName("[error] 토큰 조회중 에러 발생시 서키브레이커가 오픈되고 null 을 응답하는지 확인한다.")
        void error(CapturedOutput output) {
            // given
            String key = "token::email-userAgent2-2";
            redisTemplate.opsForValue().set(key, "errorValue");

            // when
            Token savedToken = adapter.findTokenByEmailAndUserAgent("email", "userAgent2-2");
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("redis");

            // then
            assert savedToken == null;
            assert output.toString().contains("[findByEmailAndUserAgentFallback] call - ");
            assert circuitBreaker.getState().equals(CircuitBreaker.State.OPEN);

            // cleanup
            circuitBreaker.reset();
        }
    }

    @Nested
    @DisplayName("[deleteTokenByEmail] 이메일로 토큰을 삭제하는 메소드")
    class Describe_deleteTokenByEmail {

        @Test
        @DisplayName("[success] 토큰이 정상적으로 삭제되는지 확인한다.")
        void success() {
            // given
            Token token = Token.builder()
                .id(1234L)
                .accountId(11111L)
                .email("email")
                .userAgent("userAgent3")
                .refreshToken("refreshToken")
                .regDateTime("regDateTime")
                .role("role")
                .build();
            String key = "token::email-userAgent3";
            redisTemplate.opsForValue().set(key, toJsonString(token));

            // when
            adapter.deleteTokenByEmail("email");
            String savedToken = redisTemplate.opsForValue().get(key);

            // then
            assert savedToken == null;
        }
    }
}
package com.account.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(
    Key key,
    Ttl ttl
) {
    public record Key(
        String token
    ) {
    }

    public record Ttl(
        Long accessToken,
        Long refreshToken
    ) {
    }
}

package com.order.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(
    Key key,
    Ttl ttl
) {
    public record Key(
        String customerOrder
    ) {
    }

    public record Ttl(
        Long customerOrder
    ) {
    }
}

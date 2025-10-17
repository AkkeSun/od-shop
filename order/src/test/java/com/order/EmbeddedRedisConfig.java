package com.order;

import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Configuration
@Profile("test")
public class EmbeddedRedisConfig {

    static RedisServer redisServer;

    @BeforeAll
    static void startRedis() throws IOException {
        redisServer = new RedisServer(9999);
        redisServer.start();
    }

    @AfterAll
    static void stopRedis() {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }
}

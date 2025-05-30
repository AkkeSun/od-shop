package com.accountagent.infrastructure.config;

import com.mongodb.client.MongoClient;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "10s")
public class ShedLockConfig {

    @Bean
    public LockProvider lockProvider(MongoClient mongo) {
        return new MongoLockProvider(mongo.getDatabase("account"));
    }
}

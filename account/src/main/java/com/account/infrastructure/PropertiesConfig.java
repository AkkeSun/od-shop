package com.account.infrastructure;

import com.account.infrastructure.properties.KafkaTopicProperties;
import com.account.infrastructure.properties.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    KafkaTopicProperties.class,
    RedisProperties.class
})
public class PropertiesConfig {
}

package com.order.infrastructure;

import com.order.infrastructure.properties.KafkaTopicProperties;
import com.order.infrastructure.properties.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    KafkaTopicProperties.class,
    RedisProperties.class
})
public class PropertiesConfig {
}

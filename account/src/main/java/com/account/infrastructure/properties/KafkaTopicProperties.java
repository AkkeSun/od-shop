package com.account.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.topic")
public record KafkaTopicProperties(
    String history,
    String delete,
    String login
) {
}

package com.order.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.topic")
public record KafkaTopicProperties(
    String cancelReserve,
    String rollbackReserve,
    String cancelOrder
) {
}

package com.order.fakeClass;

import com.order.infrastructure.properties.KafkaTopicProperties;
import com.order.infrastructure.properties.RedisProperties;

public class TestPropertiesHelper {

    public static RedisProperties createRedisProperties() {
        return new RedisProperties(
            new RedisProperties.Key("customer-order::%s-%s-%s"),
            new RedisProperties.Ttl(600000L)
        );
    }

    public static KafkaTopicProperties createKafkaProperties() {
        return new KafkaTopicProperties(
            "cancel-reserve", "rollback-reserve", "cancel-order");
    }
}

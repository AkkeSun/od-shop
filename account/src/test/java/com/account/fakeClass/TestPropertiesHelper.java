package com.account.fakeClass;

import com.account.infrastructure.properties.KafkaTopicProperties;
import com.account.infrastructure.properties.RedisProperties;

public class TestPropertiesHelper {

    public static RedisProperties createRedisProperties() {
        return new RedisProperties(
            new RedisProperties.Key("token::%s-%s"),
            new RedisProperties.Ttl(600000L, 99999999L)
        );
    }

    public static KafkaTopicProperties createKafkaProperties() {
        return new KafkaTopicProperties(
             "account-history", "delete-account", "account-login");
    }
}

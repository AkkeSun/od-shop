package com.productagent.adapter.out.producer;

import com.productagent.application.port.out.MessageProducerPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductProducerAdapter implements MessageProducerPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @CircuitBreaker(name = "kafka", fallbackMethod = "sendMessageFallback")
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message)
            .thenAccept(sendResult -> log.info("[{}] ==> {}", topic, message))
            .exceptionally(ex -> {
                log.error("]");
                return null;
            });
    }

    public void sendMessageFallback(String topic, String message, Throwable ex) {
        log.error("kafka sendMessage Error : " + topic);
        redisTemplate.opsForValue().set(topic + "-" + UUID.randomUUID(), message,
            Duration.ofMinutes(10));
    }
}

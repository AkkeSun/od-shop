package com.productagent.infrastructure.handler;

public interface KafkaBatchHandler {

    void handle(String topic, KafkaBatchProcess handler);
}

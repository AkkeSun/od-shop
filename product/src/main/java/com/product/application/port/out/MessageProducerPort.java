package com.product.application.port.out;

public interface MessageProducerPort {

    void sendMessage(String topic, String message);
}

package com.order.application.port.out;

public interface MessageProducerPort {

    void sendMessage(String topic, String message);
}

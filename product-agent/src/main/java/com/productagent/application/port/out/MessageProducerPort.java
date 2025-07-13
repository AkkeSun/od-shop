package com.productagent.application.port.out;

public interface MessageProducerPort {

    void sendMessage(String topic, String message);
}

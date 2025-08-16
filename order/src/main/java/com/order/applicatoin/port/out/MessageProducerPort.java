package com.order.applicatoin.port.out;

public interface MessageProducerPort {

    void sendMessage(String topic, String message);
}

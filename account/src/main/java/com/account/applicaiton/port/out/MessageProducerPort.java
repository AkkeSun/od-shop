package com.account.applicaiton.port.out;

public interface MessageProducerPort {

    void sendMessage(String topic, String message);
}

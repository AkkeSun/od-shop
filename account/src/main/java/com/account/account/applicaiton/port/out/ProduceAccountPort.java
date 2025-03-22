package com.account.account.applicaiton.port.out;

public interface ProduceAccountPort {

    void sendMessage(String topic, String message);
}

package com.order.fakeClass;

import com.order.applicatoin.port.out.MessageProducerPort;

public class DummyMessageProducerPort implements MessageProducerPort {

    @Override
    public void sendMessage(String topic, String message) {

    }
}

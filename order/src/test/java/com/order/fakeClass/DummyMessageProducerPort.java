package com.order.fakeClass;

import com.order.applicatoin.port.out.MessageProducerPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyMessageProducerPort implements MessageProducerPort {

    @Override
    public void sendMessage(String topic, String message) {
        log.info(message);
    }
}

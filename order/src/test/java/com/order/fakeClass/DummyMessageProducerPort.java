package com.order.fakeClass;

import com.order.application.port.out.MessageProducerPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyMessageProducerPort implements MessageProducerPort {

    @Override
    public void sendMessage(String topic, String message) {
        log.info("DummyMessageProducerPort");
        log.info(message);
    }
}

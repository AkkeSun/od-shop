package com.product.fakeClass;

import com.product.application.port.out.MessageProducerPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyMessageProducerPort implements MessageProducerPort {

    @Override
    public void sendMessage(String topic, String message) {
        log.info("[{}] ==> {}", topic, message);
    }
}

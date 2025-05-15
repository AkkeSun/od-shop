package com.account.fakeClass;

import com.account.applicaiton.port.out.MessageProducerPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyMessageProducerPortClass implements MessageProducerPort {

    @Override
    public void sendMessage(String topic, String message) {
        log.info("[{}] ==> {}", topic, message);
    }
}

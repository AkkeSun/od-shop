package com.productagent.domain.model;

import com.productagent.infrastructure.util.DateUtil;
import lombok.Builder;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Builder
public record DlqLog(
    String topic,
    String payload,
    String regDateTime
) {

    public static DlqLog of(ConsumerRecord<String, String> record) {
        return DlqLog.builder()
            .topic(record.topic())
            .payload(record.value())
            .regDateTime(DateUtil.getCurrentDateTime())
            .build();
    }
}

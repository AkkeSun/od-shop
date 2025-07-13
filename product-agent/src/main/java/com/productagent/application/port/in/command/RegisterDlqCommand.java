package com.productagent.application.port.in.command;

import com.productagent.domain.model.DlqLog;
import com.productagent.infrastructure.util.DateUtil;
import lombok.Builder;

@Builder
public record RegisterDlqCommand(
    String topic,
    String payload
) {

    public DlqLog toDomain() {
        return DlqLog.builder()
            .topic(topic)
            .payload(payload)
            .regDateTime(DateUtil.getCurrentDateTime())
            .build();
    }
}

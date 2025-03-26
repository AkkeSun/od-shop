package com.accountagent.domain.model;

import lombok.Builder;

@Builder
public record DlqLog(
    String topic,
    String payload,
    String regDateTime
) {

}

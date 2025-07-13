package com.productagent.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.productagent.infrastructure.util.DateUtil;
import lombok.Builder;

@Builder
public record DlqLog(
    String topic,
    String payload,
    String regDateTime
) {

    public static DlqLog of(JsonNode jsonNode) {
        return DlqLog.builder()
            .topic(jsonNode.get("topic").asText())
            .payload(jsonNode.get("value").asText())
            .regDateTime(DateUtil.getCurrentDateTime())
            .build();
    }
}

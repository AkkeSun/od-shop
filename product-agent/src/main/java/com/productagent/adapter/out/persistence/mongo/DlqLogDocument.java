package com.productagent.adapter.out.persistence.mongo;

import com.productagent.domain.model.DlqLog;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
record DlqLogDocument(
    String topic,
    String payload,
    String regDateTime
) {

    static DlqLogDocument of(DlqLog domain) {
        return DlqLogDocument.builder()
            .topic(domain.topic())
            .payload(domain.payload())
            .regDateTime(domain.regDateTime())
            .build();
    }
}

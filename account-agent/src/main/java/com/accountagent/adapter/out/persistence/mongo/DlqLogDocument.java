package com.accountagent.adapter.out.persistence.mongo;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
record DlqLogDocument(
    String topic,
    String payload,
    String regDateTime
) {

}

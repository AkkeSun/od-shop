package com.accountagent.adapter.out.persistence.mongo;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
record AccountHistoryDocument(
    Long accountId,
    String type,
    String detailInfo,
    String regDateTime
) {

}

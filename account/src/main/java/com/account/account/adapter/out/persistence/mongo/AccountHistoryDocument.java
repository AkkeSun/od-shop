package com.account.account.adapter.out.persistence.mongo;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
public record AccountHistoryDocument (
    Long accountId,
    String type,
    String detailInfo,
    String regDate,
    String regDateTime
) {

}

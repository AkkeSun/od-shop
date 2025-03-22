package com.account.token.adapter.out.persistence.mongo;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
record LoginLogDocument(
    Long accountId,
    String email,
    String loginDateTime
) {

}

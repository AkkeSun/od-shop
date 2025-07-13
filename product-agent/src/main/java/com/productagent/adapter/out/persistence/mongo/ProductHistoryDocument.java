package com.productagent.adapter.out.persistence.mongo;

import com.productagent.domain.model.ProductHistory;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
record ProductHistoryDocument(
    Long productId,
    Long accountId,
    String type,
    String detailInfo,
    String regDateTime
) {

    static ProductHistoryDocument of(ProductHistory domain) {
        return ProductHistoryDocument.builder()
            .productId(domain.productId())
            .accountId(domain.accountId())
            .type(domain.type())
            .detailInfo(domain.detailInfo())
            .regDateTime(domain.regDateTime())
            .build();
    }
}

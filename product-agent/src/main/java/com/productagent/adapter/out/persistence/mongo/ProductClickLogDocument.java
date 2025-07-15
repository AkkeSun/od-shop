package com.productagent.adapter.out.persistence.mongo;

import com.productagent.domain.model.ProductClickLog;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
record ProductClickLogDocument(
    Long productId,
    String regDateTime
) {

    static ProductClickLogDocument of(ProductClickLog domain) {
        return ProductClickLogDocument.builder()
            .productId(domain.productId())
            .regDateTime(domain.regDateTime())
            .build();
    }
}

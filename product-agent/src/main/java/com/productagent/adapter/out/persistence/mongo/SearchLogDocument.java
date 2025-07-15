package com.productagent.adapter.out.persistence.mongo;

import com.productagent.domain.model.SearchLog;
import lombok.Builder;

@Builder
record SearchLogDocument(
    String query,
    String regDateTime
) {

    static SearchLogDocument of(SearchLog domain) {
        return SearchLogDocument.builder()
            .query(domain.query())
            .regDateTime(domain.regDateTime())
            .build();
    }
}

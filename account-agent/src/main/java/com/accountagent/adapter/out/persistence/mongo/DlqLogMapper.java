package com.accountagent.adapter.out.persistence.mongo;

import com.accountagent.domain.model.DlqLog;

class DlqLogMapper {

    static DlqLogDocument toDocument(DlqLog domain) {
        return DlqLogDocument.builder()
            .topic(domain.topic())
            .payload(domain.payload())
            .regDateTime(domain.regDateTime())
            .build();
    }
}

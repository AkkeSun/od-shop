package com.accountagent.adapter.out.persistence.mongo;

import com.accountagent.domain.model.AccountHistory;

class AccountHistoryMapper {

    public static AccountHistoryDocument toDocument(AccountHistory domain) {
        return AccountHistoryDocument.builder()
            .accountId(domain.accountId())
            .type(domain.type())
            .detailInfo(domain.detailInfo())
            .regDateTime(domain.regDateTime())
            .build();
    }
}

package com.accountagent.adapter.out.persistence.mongo;

import com.accountagent.domain.model.AccountHistory;
import org.springframework.stereotype.Component;

@Component
class AccountHistoryMapper {

    public AccountHistoryDocument toDocument(AccountHistory domain) {
        return AccountHistoryDocument.builder()
            .accountId(domain.accountId())
            .type(domain.type())
            .detailInfo(domain.detailInfo())
            .regDateTime(domain.regDateTime())
            .build();
    }
}

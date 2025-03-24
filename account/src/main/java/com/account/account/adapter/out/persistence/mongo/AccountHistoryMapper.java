package com.account.account.adapter.out.persistence.mongo;

import com.account.account.domain.model.AccountHistory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
class AccountHistoryMapper {

    public AccountHistoryDocument toDocument(AccountHistory domain) {
        return AccountHistoryDocument.builder()
            .accountId(domain.accountId())
            .type(domain.type())
            .detailInfo(domain.detailInfo())
            .regDate(domain.regDate())
            .regDateTime(domain.regDateTime()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")))
            .build();
    }

    public AccountHistory toDomain(AccountHistoryDocument document) {
        return AccountHistory.builder()
            .accountId(document.accountId())
            .type(document.type())
            .detailInfo(document.detailInfo())
            .regDate(document.regDate())
            .regDateTime(LocalDateTime.parse(document.regDateTime(),
                DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")))
            .build();
    }
}

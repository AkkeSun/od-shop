package com.account.account.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountHistory {

    private Long accountId;

    private String type;

    private String detailInfo;

    private String regDate;

    private LocalDateTime regDateTime;

    @Builder
    public AccountHistory(Long accountId, String type, String detailInfo,
        String regDate, LocalDateTime regDateTime) {
        this.accountId = accountId;
        this.type = type;
        this.detailInfo = detailInfo;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }

    public AccountHistory createAccountHistoryForDelete(Long accountId) {
        return AccountHistory.builder()
            .accountId(accountId)
            .type("delete")
            .regDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .regDateTime(LocalDateTime.now())
            .build();
    }

    public AccountHistory createAccountHistoryForUpdate(Long accountId, String updateList) {
        return AccountHistory.builder()
            .accountId(accountId)
            .type("update")
            .detailInfo(updateList)
            .regDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .regDateTime(LocalDateTime.now())
            .build();
    }
}

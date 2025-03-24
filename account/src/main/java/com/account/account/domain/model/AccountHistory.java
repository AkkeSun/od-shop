package com.account.account.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@Builder
public record AccountHistory(
    Long accountId,
    String type,
    String detailInfo,
    String regDate,
    LocalDateTime regDateTime
) {

    public static AccountHistory createAccountHistoryForDelete(Long accountId) {
        return AccountHistory.builder()
            .accountId(accountId)
            .type("delete")
            .regDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .regDateTime(LocalDateTime.now())
            .build();
    }

    public static AccountHistory createAccountHistoryForUpdate(Long accountId, String updateList) {
        return AccountHistory.builder()
            .accountId(accountId)
            .type("update")
            .detailInfo(updateList)
            .regDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .regDateTime(LocalDateTime.now())
            .build();
    }
}

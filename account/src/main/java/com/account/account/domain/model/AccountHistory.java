package com.account.account.domain.model;

import static com.account.global.util.DateUtil.getCurrentDateTime;

import lombok.Builder;

@Builder
public record AccountHistory(
    Long accountId,
    String type,
    String detailInfo,
    String regDateTime
) {

    public static AccountHistory createAccountHistoryForDelete(Long accountId) {
        return AccountHistory.builder()
            .accountId(accountId)
            .type("delete")
            .regDateTime(getCurrentDateTime())
            .build();
    }

    public static AccountHistory createAccountHistoryForUpdate(Long accountId, String updateList) {
        return AccountHistory.builder()
            .accountId(accountId)
            .type("update")
            .detailInfo(updateList)
            .regDateTime(getCurrentDateTime())
            .build();
    }
}

package com.accountagent.domain.model;

import lombok.Builder;

@Builder
public record AccountHistory(
    Long accountId,
    String type,
    String detailInfo,
    String regDateTime
) {

}

package com.product.application.port.in.command;

import lombok.Builder;

@Builder
public record FindRecommendProductCommand(
    String searchDate,
    Long accountId
) {

    public boolean needsPersonalRecommend() {
        return accountId != null;
    }
}

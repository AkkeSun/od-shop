package com.product.domain.model;

import static com.product.infrastructure.util.DateUtil.formatDateTime;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ProductHistory(
    Long productId,
    Long accountId,
    String type,
    String detailInfo,
    String regDateTime
) {

    public static ProductHistory createProductHistoryForDelete(Product product,
        LocalDateTime deleteAt) {
        return ProductHistory.builder()
            .accountId(product.getSellerId())
            .productId(product.getId())
            .type("delete")
            .detailInfo(product.getProductName())
            .regDateTime(formatDateTime(deleteAt))
            .build();
    }

    public static ProductHistory createProductHistoryForUpdate(Product product,
        List<String> updateList) {
        return ProductHistory.builder()
            .accountId(product.getSellerId())
            .productId(product.getId())
            .type("update")
            .detailInfo(String.join(",", updateList))
            .regDateTime(formatDateTime(product.getUpdateDateTime()))
            .build();
    }

    public static ProductHistory createProductHistoryForUpdateQuantity(Product product,
        long quantity) {
        return ProductHistory.builder()
            .accountId(product.getSellerId())
            .productId(product.getId())
            .type("update")
            .detailInfo("add quantity " + quantity)
            .regDateTime(formatDateTime(product.getUpdateDateTime()))
            .build();
    }
}

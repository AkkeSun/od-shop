package com.product.domain.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            .regDateTime(deleteAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();
    }

    public static ProductHistory createProductHistoryForUpdate(Product product,
        List<String> updateList) {
        return ProductHistory.builder()
            .accountId(product.getSellerId())
            .productId(product.getId())
            .type("update")
            .detailInfo(String.join(",", updateList))
            .regDateTime(product.getUpdateDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();
    }
}

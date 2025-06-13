package com.product.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProductMetric(
    Long metricId,
    Product product,
    long salesCount, // 판매량
    long reviewCount, // 리뷰수
    long hitCount, // 조회수
    double reviewScore, // 리뷰 평점
    double totalScore, // 총 평점
    boolean needsEsUpdate, // ES 업데이트 필요 여부
    LocalDate regDate,
    LocalDateTime regDateTime,
    LocalDateTime updateTime
) {

    public static ProductMetric of(Product product) {
        return ProductMetric.builder()
            .metricId(product.getId())
            .product(product)
            .salesCount(0)
            .reviewCount(0)
            .hitCount(0)
            .reviewScore(0)
            .totalScore(0)
            .needsEsUpdate(false)
            .regDate(LocalDate.now())
            .regDateTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
    }
}

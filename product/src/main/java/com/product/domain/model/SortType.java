package com.product.domain.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SortType {

    PRICE_ASC("price", false, "가격 낮은순"),
    PRICE_DESC("price", true, "가격 높은순"),
    SALES_COUNT_ASC("salesCount", false, "판매량 낮은순"),
    SALES_COUNT_DESC("salesCount", true, "판매량 높은순"),
    REVIEW_CNT_ASC("reviewCount", false, "리뷰 적은순"),
    REVIEW_CNT_DESC("reviewCount", true, "리뷰 많은순"),
    REG_DATE_TIME_ASC("regDateTime", false, "등록일 오래된순"),
    REG_DATE_TIME_DESC("regDateTime", true, "등록일 최신순"),
    TOTAL_SCORE_ASC("totalScore", false, "추천 낮은순"),
    TOTAL_SCORE_DESC("totalScore", true, "추천 높은순"),

    ;

    private final String type;
    private final boolean isDecending;
    private final String description;

    public String type() {
        return type;
    }

    public boolean isDescending() {
        return isDecending;
    }
}

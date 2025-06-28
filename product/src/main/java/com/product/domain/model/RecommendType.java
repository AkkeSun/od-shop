package com.product.domain.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RecommendType {

    PERSONAL("개인 맞춤형"),
    POPULAR("인기 상품"),
    TREND("트렌드 상품"),
    ;

    private final String description;

}

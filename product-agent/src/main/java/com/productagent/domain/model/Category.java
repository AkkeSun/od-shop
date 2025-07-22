package com.productagent.domain.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Category {
    DIGITAL("전자제품"),
    FASHION("패션"),
    SPORTS("스포츠"),
    FOOD("식품"),
    LIFE("생활용품"),
    TOTAL("전체");

    private final String description;

    public String description() {
        return description;
    }
}

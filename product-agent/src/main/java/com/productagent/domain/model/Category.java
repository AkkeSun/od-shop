package com.productagent.domain.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Category {
    ELECTRONICS("전자제품"),
    FASHION("패션"),
    HOME_APPLIANCES("가전제품"),
    BEAUTY("뷰티"),
    BOOKS("도서"),
    SPORTS("스포츠"),
    FOOD("식품"),
    TOYS("장난감"),
    FURNITURE("가구"),
    AUTOMOTIVE("자동차 용품"),
    HEALTH("건강"),
    TOTAL("전체");

    private final String description;

    public String description() {
        return description;
    }
}

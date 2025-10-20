package com.product.adapter.in.controller.find_product_list;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.validation.Contains;
import com.common.infrastructure.validation.groups.ValidationGroups.CustomGroups;
import com.common.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.application.port.in.command.FindProductListCommand;
import com.product.domain.model.Category;
import com.product.domain.model.SortType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class FindProductListRequest {

    @NotBlank(message = "검색어는 필수값 입니다", groups = NotBlankGroups.class)
    private String query;

    @Contains(
        values = {"DIGITAL", "FASHION", "SPORTS", "FOOD", "LIFE", "TOTAL"},
        groups = CustomGroups.class, message = "존재하지 않은 카테고리 입니다")
    @NotBlank(message = "카테고리는 필수값 입니다", groups = NotBlankGroups.class)
    private String category;

    @Contains(
        values = {"PRICE_ASC", "PRICE_DESC", "SALES_COUNT_ASC", "SALES_COUNT_DESC",
            "REVIEW_CNT_ASC", "REVIEW_CNT_DESC", "REG_DATE_TIME_ASC", "REG_DATE_TIME_DESC",
            "TOTAL_SCORE_ASC", "TOTAL_SCORE_DESC"},
        groups = CustomGroups.class, message = "존재하지 않은 정렬 타입 입니다")
    @NotBlank(message = "정렬 타입은 필수값 입니다", groups = NotBlankGroups.class)
    private String sortType;

    private int page;

    private int size;

    FindProductListCommand toCommand() {
        return FindProductListCommand.builder()
            .query(query)
            .category(Category.valueOf(category))
            .sortType(SortType.valueOf(sortType))
            .page(page)
            .size(size == 0 ? 10 : size)
            .build();
    }

    @Override
    public String toString() {
        return toJsonString(this);
    }
}

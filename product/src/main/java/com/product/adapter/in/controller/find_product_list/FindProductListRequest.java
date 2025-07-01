package com.product.adapter.in.controller.find_product_list;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.domain.model.Category;
import com.product.domain.model.SortType;
import com.product.infrastructure.validation.ValidCategory;
import com.product.infrastructure.validation.ValidSortType;
import com.product.infrastructure.validation.groups.ValidationGroups.CustomGroups;
import com.product.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindProductListRequest {

    @NotBlank(message = "검색어는 필수값 입니다", groups = NotBlankGroups.class)
    private String query;

    @ValidCategory(groups = CustomGroups.class)
    @NotBlank(message = "카테고리는 필수값 입니다", groups = NotBlankGroups.class)
    private String category;

    @ValidSortType(groups = CustomGroups.class)
    @NotBlank(message = "정렬 타입은 필수값 입니다", groups = NotBlankGroups.class)
    private String sortType;

    private int page;

    private int size;

    @Builder
    FindProductListRequest(String query, String sortType, String category, int page,
        int size,
        boolean isDesc) {
        this.query = query;
        this.sortType = sortType;
        this.category = category;
        this.page = page;
        this.size = size;
    }

    FindProductListCommand toCommand() {
        SortType type = SortType.valueOf(sortType);
        return FindProductListCommand.builder()
            .query(query)
            .category(Category.valueOf(category))
            .sortType(type)
            .page(page)
            .size(size == 0 ? 10 : size)
            .build();
    }
}

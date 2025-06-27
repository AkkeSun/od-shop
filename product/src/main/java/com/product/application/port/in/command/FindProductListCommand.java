package com.product.application.port.in.command;

import com.product.domain.model.Category;
import com.product.domain.model.SortType;
import lombok.Builder;

@Builder
public record FindProductListCommand(
    String query,
    Category category,
    SortType sortType,
    int page,
    int size
) {

    public boolean isTotalCategorySearch() {
        return category.equals(Category.TOTAL);
    }
}

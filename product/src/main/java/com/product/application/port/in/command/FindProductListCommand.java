package com.product.application.port.in.command;

import com.product.domain.model.Category;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;

@Builder
public record FindProductListCommand(
    String query,
    Category category,
    PageRequest pageable
) {

    public boolean isTotalCategorySearch() {
        return category.equals(Category.TOTAL);
    }
}

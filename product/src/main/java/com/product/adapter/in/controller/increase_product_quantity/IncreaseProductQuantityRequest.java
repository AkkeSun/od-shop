package com.product.adapter.in.controller.increase_product_quantity;

import com.product.application.port.in.command.IncreaseProductQuantityCommand;
import com.product.domain.model.Account;
import com.product.infrastructure.validation.groups.ValidationGroups.SizeGroups;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
record IncreaseProductQuantityRequest(
    @Min(value = 1, message = "상품 수량은 1 이상 이어야 합니다", groups = SizeGroups.class)
    int quantity
) {

    IncreaseProductQuantityCommand of(Account account) {
        return IncreaseProductQuantityCommand.builder()
            .quantity(quantity)
            .account(account)
            .build();
    }
}

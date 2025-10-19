package com.product.adapter.in.controller.increase_product_quantity;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.validation.groups.ValidationGroups.SizeGroups;
import com.product.application.port.in.command.IncreaseProductQuantityCommand;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
record IncreaseProductQuantityRequest(
    @Min(value = 1, message = "상품 수량은 1 이상 이어야 합니다", groups = SizeGroups.class)
    int quantity
) {

    IncreaseProductQuantityCommand of(LoginAccountInfo loginInfo) {
        return IncreaseProductQuantityCommand.builder()
            .quantity(quantity)
            .loginInfo(loginInfo)
            .build();
    }

    @Override
    public String toString() {
        return toJsonString(this);
    }
}

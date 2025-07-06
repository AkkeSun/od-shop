package com.product.adapter.in.controller.update_product_quantity;

import com.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.domain.model.Account;
import com.product.domain.model.QuantityType;
import com.product.infrastructure.validation.ValidQuantityType;
import com.product.infrastructure.validation.groups.ValidationGroups.CustomGroups;
import com.product.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.infrastructure.validation.groups.ValidationGroups.SizeGroups;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
record UpdateProductQuantityRequest(

    @Min(value = 1, message = "상품 수량은 1 이상 이어야 합니다", groups = SizeGroups.class)
    Integer quantity,

    @ValidQuantityType(groups = CustomGroups.class)
    @NotBlank(message = "수정 타입은 필수 값 입니다", groups = NotBlankGroups.class)
    String quantityType
) {

    UpdateProductQuantityCommand of(Long productId, Account account) {
        return UpdateProductQuantityCommand.builder()
            .productId(productId)
            .quantity(quantity)
            .type(QuantityType.valueOf(quantityType))
            .account(account)
            .build();
    }
}

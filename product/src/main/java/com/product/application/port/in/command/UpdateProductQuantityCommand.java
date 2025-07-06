package com.product.application.port.in.command;

import static com.product.domain.model.QuantityType.ADD_QUANTITY;
import static com.product.domain.model.QuantityType.PURCHASE;

import com.product.domain.model.Account;
import com.product.domain.model.QuantityType;
import lombok.Builder;

@Builder
public record UpdateProductQuantityCommand(
    Long productId,
    long quantity,
    QuantityType type,
    Account account
) {

    public boolean isAddQuantity() {
        return type.equals(ADD_QUANTITY);
    }

    public boolean isPurchase() {
        return type.equals(PURCHASE);
    }
}

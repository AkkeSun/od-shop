package com.product.application.port.in.command;

import com.product.domain.model.Account;
import lombok.Builder;

@Builder
public record RegisterProductCommand(
    Account account,
    String productName,
    String productImgUrl,
    String descriptionImgUrl,
    long price,
    long quantity,
    String category
) {

}

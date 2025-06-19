package com.product.application.port.in.command;

import com.product.domain.model.Account;
import java.util.Set;
import lombok.Builder;

@Builder
public record UpdateProductCommand(
    Account account,
    Long productId,
    String productName,
    String productImgUrl,
    String descriptionImgUrl,
    Set<String> productOption,
    Set<String> keywords,
    long price,
    long quantity
) {

}

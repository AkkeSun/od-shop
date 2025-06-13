package com.product.application.port.in.command;

import com.product.domain.model.Account;
import java.util.Set;
import lombok.Builder;

@Builder
public record RegisterProductCommand(
    Account account,
    String productName,
    String productImgUrl,
    String descriptionImgUrl,
    Set<String> productOption,
    Set<String> keywords,
    long price,
    long quantity,
    String category
) {

}

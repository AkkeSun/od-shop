package com.product.application.port.in.command;

import com.common.infrastructure.resolver.LoginAccountInfo;
import lombok.Builder;

@Builder
public record RegisterProductCommand(
    LoginAccountInfo loginInfo,
    String productName,
    String productImgUrl,
    String descriptionImgUrl,
    long price,
    long quantity,
    String category
) {

}

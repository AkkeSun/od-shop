package com.product.application.port.in.command;

import com.common.infrastructure.resolver.LoginAccountInfo;
import java.util.Set;
import lombok.Builder;

@Builder
public record UpdateProductCommand(
    LoginAccountInfo loginInfo,
    Long productId,
    String productName,
    String productImgUrl,
    String descriptionImgUrl,
    Set<String> keywords,
    long price
) {

}

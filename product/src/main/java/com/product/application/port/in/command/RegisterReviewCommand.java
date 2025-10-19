package com.product.application.port.in.command;

import com.common.infrastructure.resolver.LoginAccountInfo;
import lombok.Builder;

@Builder
public record RegisterReviewCommand(
    LoginAccountInfo loginInfo,
    Long productId,
    double score,
    String review
) {

}

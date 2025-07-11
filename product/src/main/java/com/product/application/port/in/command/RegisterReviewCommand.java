package com.product.application.port.in.command;

import com.product.domain.model.Account;
import lombok.Builder;

@Builder
public record RegisterReviewCommand(
    Account account,
    Long productId,
    double score,
    String review
) {

}

package com.product.application.port.in.command;

import com.product.domain.model.Account;
import lombok.Builder;

@Builder
public record RegisterCommentCommand(
    Account account,
    Long productId,
    double score,
    String comment
) {

}

package com.product.application.service.register_comment;

import lombok.Builder;

@Builder
public record RegisterCommentServiceResponse(
    boolean result
) {

    public static RegisterCommentServiceResponse ofSuccess() {
        return RegisterCommentServiceResponse.builder()
            .result(true)
            .build();
    }
}

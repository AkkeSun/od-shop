package com.product.adapter.in.controller.register_comment;

import com.product.application.service.register_comment.RegisterCommentServiceResponse;
import lombok.Builder;

@Builder
record RegisterCommentResponse(
    Boolean result
) {

    static RegisterCommentResponse of(RegisterCommentServiceResponse serviceResponse) {
        return RegisterCommentResponse.builder()
            .result(serviceResponse.result())
            .build();
    }
}

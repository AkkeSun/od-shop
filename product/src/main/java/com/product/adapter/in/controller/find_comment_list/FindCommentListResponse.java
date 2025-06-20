package com.product.adapter.in.controller.find_comment_list;

import com.product.application.service.find_comment_list.FindCommentListServiceResponse;
import lombok.Builder;

@Builder
record FindCommentListResponse(
    String customerEmail,
    String comment
) {

    static FindCommentListResponse of(FindCommentListServiceResponse serviceResponse) {
        return FindCommentListResponse.builder()
            .customerEmail(serviceResponse.customerEmail())
            .comment(serviceResponse.comment())
            .build();
    }
}

package com.product.application.service.find_comment_list;

import com.product.domain.model.Comment;
import lombok.Builder;

@Builder
public record FindCommentListServiceResponse(
    String customerEmail,
    String comment
) {

    static FindCommentListServiceResponse of(Comment comment) {
        return FindCommentListServiceResponse.builder()
            .customerEmail(comment.customerEmail())
            .comment(comment.comment())
            .build();
    }
}

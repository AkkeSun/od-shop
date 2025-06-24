package com.product.application.service.find_comment_list;

import com.product.application.port.in.command.FindCommentListCommand;
import com.product.domain.model.Comment;
import java.util.List;
import lombok.Builder;

@Builder
public record FindCommentListServiceResponse(
    Long productId,
    int page,
    int size,
    int commentCount,
    List<FindCommentListServiceResponseItem> comments

) {

    static FindCommentListServiceResponse of(List<Comment> comments,
        FindCommentListCommand command) {
        return FindCommentListServiceResponse.builder()
            .productId(command.productId())
            .page(command.pageable().getPageNumber())
            .size(command.pageable().getPageSize())
            .commentCount(comments.size())
            .comments(comments.stream()
                .map(FindCommentListServiceResponseItem::of)
                .toList())
            .build();
    }

    @Builder
    public record FindCommentListServiceResponseItem(
        String customerEmail,
        String comment
    ) {

        static FindCommentListServiceResponseItem of(Comment comment) {
            return FindCommentListServiceResponseItem.builder()
                .customerEmail(comment.customerEmail())
                .comment(comment.comment())
                .build();
        }
    }
}

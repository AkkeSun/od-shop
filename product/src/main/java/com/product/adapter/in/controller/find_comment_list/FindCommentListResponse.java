package com.product.adapter.in.controller.find_comment_list;

import com.product.application.service.find_comment_list.FindCommentListServiceResponse;
import com.product.application.service.find_comment_list.FindCommentListServiceResponse.FindCommentListServiceResponseItem;
import java.util.List;
import lombok.Builder;

@Builder
record FindCommentListResponse(
    Long productId,
    int page,
    int size,
    int commentCount,
    List<FindCommentListResponseItem> comments
) {

    static FindCommentListResponse of(FindCommentListServiceResponse serviceResponse) {
        return FindCommentListResponse.builder()
            .productId(serviceResponse.productId())
            .page(serviceResponse.page())
            .size(serviceResponse.size())
            .commentCount(serviceResponse.commentCount())
            .comments(serviceResponse.comments().stream()
                .map(FindCommentListResponseItem::of)
                .toList())
            .build();
    }

    @Builder
    record FindCommentListResponseItem(
        String customerEmail,
        String comment
    ) {

        static FindCommentListResponseItem of(FindCommentListServiceResponseItem item) {
            return FindCommentListResponseItem.builder()
                .customerEmail(item.customerEmail())
                .comment(item.comment())
                .build();
        }
    }
}

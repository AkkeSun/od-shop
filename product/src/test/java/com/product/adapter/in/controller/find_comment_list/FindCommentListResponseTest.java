package com.product.adapter.in.controller.find_comment_list;

import com.product.application.service.find_comment_list.FindCommentListServiceResponse;
import com.product.application.service.find_comment_list.FindCommentListServiceResponse.FindCommentListServiceResponseItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindCommentListResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답을 API 응답으로 변환한다.")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답을 API 응답으로 변환한다.")
        void success() {
            // given
            FindCommentListServiceResponse serviceResponse = FindCommentListServiceResponse.builder()
                .page(0)
                .size(10)
                .commentCount(1)
                .comments(List.of(FindCommentListServiceResponseItem.builder()
                    .comment("좋아요")
                    .customerEmail("test")
                    .build()))
                .build();

            // when
            FindCommentListResponse response = FindCommentListResponse.of(serviceResponse);

            // then
            assert response.page() == serviceResponse.page();
            assert response.size() == serviceResponse.size();
            assert response.commentCount() == serviceResponse.commentCount();
            assert response.comments().size() == serviceResponse.comments().size();
            assert response.comments().get(0).comment().equals("좋아요");
            assert response.comments().get(0).customerEmail().equals("test");

        }
    }
}
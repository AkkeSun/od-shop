package com.product.adapter.in.controller.find_comment_list;

import com.product.application.service.find_comment_list.FindCommentListServiceResponse;
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
                .comment("comment")
                .customerEmail("email")
                .build();

            // when
            FindCommentListResponse response = FindCommentListResponse.of(serviceResponse);

            // then
            assert response.customerEmail().equals("email");
            assert response.comment().equals("comment");
        }
    }
}
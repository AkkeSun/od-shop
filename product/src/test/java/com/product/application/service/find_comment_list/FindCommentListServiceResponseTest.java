package com.product.application.service.find_comment_list;

import com.product.domain.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindCommentListServiceResponseTest {

    @Nested
    @DisplayName("[of] 도메인을 서비스 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 도메인 객체를 서비스 응답 객체로 변환하는지 확인한다.")
        void success() {
            // given
            Comment comment = Comment.builder()
                .customerEmail("email")
                .comment("comment")
                .build();

            // when
            FindCommentListServiceResponse response = FindCommentListServiceResponse.of(comment);

            // then
            assert response.customerEmail().equals(comment.customerEmail());
            assert response.comment().equals(comment.comment());
        }
    }
}
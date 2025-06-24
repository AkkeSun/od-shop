package com.product.application.service.find_comment_list;

import com.product.application.port.in.command.FindCommentListCommand;
import com.product.domain.model.Comment;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

class FindCommentListServiceResponseTest {

    @Nested
    @DisplayName("[of] command 와 도메인 리스트로 서비스 응답 객체를 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] command 와 도메인 리스트로 서비스 응답 객체로 변환하는지 확인한다.")
        void success() {
            // given
            List<Comment> comments = List.of(Comment.builder()
                .customerEmail("email")
                .comment("comment")
                .build());
            FindCommentListCommand command = FindCommentListCommand.builder()
                .productId(1L)
                .pageable(PageRequest.of(0, 10))
                .build();

            // when
            FindCommentListServiceResponse response = FindCommentListServiceResponse.of(comments,
                command);

            // then
            assert response.productId().equals(command.productId());
            assert response.page() == command.pageable().getPageNumber();
            assert response.size() == command.pageable().getPageSize();
            assert response.commentCount() == comments.size();
            assert response.comments().size() == comments.size();
            assert response.comments().get(0).customerEmail().equals("email");
            assert response.comments().get(0).comment().equals("comment");
        }
    }
}
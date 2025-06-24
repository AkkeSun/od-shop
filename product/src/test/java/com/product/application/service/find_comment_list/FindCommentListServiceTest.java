package com.product.application.service.find_comment_list;

import com.product.application.port.in.command.FindCommentListCommand;
import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Comment;
import com.product.fakeClass.FakeCommentStoragePort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

class FindCommentListServiceTest {

    private final CommentStoragePort commentStoragePort;
    private final FindCommentListService findCommentListService;

    FindCommentListServiceTest() {
        this.commentStoragePort = new FakeCommentStoragePort();
        this.findCommentListService = new FindCommentListService(commentStoragePort);
    }

    @Nested
    @DisplayName("[findCommentList] 댓글 목록 조회 메소드")
    class Describe_findCommentList {

        @Test
        @DisplayName("[success] 댓글 목록을 성공적으로 조회하는지 확인한다.")
        void success() {
            // given
            Comment comment = Comment.builder()
                .id(1L)
                .productId(1L)
                .comment("This is a test comment")
                .customerEmail("email")
                .regDate(LocalDate.of(2025, 1, 1))
                .regDateTime(LocalDateTime.of(2025, 1, 1, 12, 0, 0))
                .build();
            commentStoragePort.register(comment);
            FindCommentListCommand command = FindCommentListCommand.builder()
                .productId(1L)
                .pageable(PageRequest.of(0, 10))
                .build();

            // when
            FindCommentListServiceResponse response = findCommentListService.findCommentList(
                command);

            // then
            assert response.productId().equals(comment.productId());
            assert response.page() == 0;
            assert response.size() == 10;
            assert response.comments().size() == 1;
            assert response.comments().get(0).comment().equals(comment.comment());
            assert response.comments().get(0).customerEmail().equals(comment.customerEmail());

        }
    }
}
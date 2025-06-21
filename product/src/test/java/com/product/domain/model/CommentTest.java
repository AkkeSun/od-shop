package com.product.domain.model;

import com.product.application.port.in.command.RegisterCommentCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Nested
    @DisplayName("[of] 도메인을 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 도메인 객체를 생성하는지 확인한다.")
        void success() {
            // given
            RegisterCommentCommand command = RegisterCommentCommand.builder()
                .productId(10L)
                .account(Account.builder().id(1L).email("od").build())
                .score(5.0)
                .comment("This is a test comment")
                .build();
            Long id = 1L;

            // when
            Comment comment = Comment.of(command, id);

            // then
            assert comment.id().equals(id);
            assert comment.productId().equals(command.productId());
            assert comment.customerId().equals(command.account().id());
            assert comment.customerEmail().equals(command.account().email());
            assert comment.score() == command.score();
            assert comment.comment().equals(command.comment());
            assert comment.regDate() != null;
            assert comment.regDateTime() != null;
        }
    }
}
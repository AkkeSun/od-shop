package com.product.adapter.out.persistence.jpa.shard2;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.IntegrationTestSupport;
import com.product.adapter.out.persistence.jpa.shard1.CommentShard1Adapter;
import com.product.application.port.in.command.FindCommentListCommand;
import com.product.domain.model.Comment;
import com.product.infrastructure.exception.CustomNotFoundException;
import com.product.infrastructure.exception.ErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentShard2AdapterTest extends IntegrationTestSupport {

    @Autowired
    CommentShard1Adapter adapter;
    @Autowired
    CommentShard2Repository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Nested
    @DisplayName("[findByProductId] 상품 ID로 리뷰를 조회하는 API")
    class Describe_findByProductId {

        @Test
        @DisplayName("[error] 리뷰가 존재하지 않을 때 예외를 응답한다.")
        void error() {
            // given
            FindCommentListCommand command = FindCommentListCommand.builder()
                .productId(2L)
                .page(0)
                .size(10)
                .build();

            // when
            CustomNotFoundException result = assertThrows(
                CustomNotFoundException.class, () -> adapter.findByProductId(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.DoesNotExist_COMMENT_INFO);
        }

        @Test
        @DisplayName("[success] 리뷰가 존재할 때 리뷰를 응답한다.")
        void success() {
            // given
            FindCommentListCommand command = FindCommentListCommand.builder()
                .productId(1L)
                .page(0)
                .size(10)
                .build();
            CommentShard2Entity entity1 = repository.save(CommentShard2Entity.builder()
                .id(123L)
                .productId(command.productId())
                .comment("test comment")
                .customerId(10L)
                .score(3.5)
                .regDate(LocalDate.of(2025, 1, 1))
                .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build());
            CommentShard2Entity entity2 = repository.save(CommentShard2Entity.builder()
                .id(124L)
                .productId(command.productId())
                .comment("another comment")
                .customerId(20L)
                .score(4.0)
                .regDate(LocalDate.of(2025, 1, 2))
                .regDateTime(LocalDateTime.of(2025, 1, 2, 0, 0, 0))
                .build());

            // when
            List<Comment> result = adapter.findByProductId(command);

            // then
            assert result.size() == 2;
            assert result.get(0).id().equals(entity1.getId());
            assert result.get(1).id().equals(entity2.getId());
            assert result.get(0).comment().equals(entity1.getComment());
            assert result.get(1).comment().equals(entity2.getComment());
            assert result.get(0).productId().equals(entity1.getProductId());
            assert result.get(1).productId().equals(entity2.getProductId());
            assert result.get(0).customerId().equals(entity1.getCustomerId());
            assert result.get(1).customerId().equals(entity2.getCustomerId());
            assert result.get(0).score() == entity1.getScore();
            assert result.get(1).score() == entity2.getScore();
            assert result.get(0).regDate().equals(entity1.getRegDate());
            assert result.get(1).regDate().equals(entity2.getRegDate());
            assert result.get(0).regDateTime().equals(entity1.getRegDateTime());
            assert result.get(1).regDateTime().equals(entity2.getRegDateTime());
            assert result.get(0).id().equals(entity1.getId());
            assert result.get(1).id().equals(entity2.getId());
            assert result.get(0).regDate().equals(entity1.getRegDate());
            assert result.get(1).regDate().equals(entity2.getRegDate());
            assert result.get(0).regDateTime().equals(entity1.getRegDateTime());
            assert result.get(1).regDateTime().equals(entity2.getRegDateTime());
        }
    }

    @Nested
    @DisplayName("[register] 리뷰를 등록하는 API")
    class Describe_register {

        @Test
        @DisplayName("[success] 리뷰를 등록한다.")
        void success() {
            // given
            Comment comment = Comment.builder()
                .id(123L)
                .productId(1L)
                .customerId(10L)
                .comment("Great product!")
                .score(4.5)
                .regDate(LocalDate.of(2025, 1, 1))
                .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();

            // when
            adapter.register(comment);
            CommentShard2Entity entity = repository.findById(comment.id()).get();

            // then
            assert entity.getId().equals(comment.id());
            assert entity.getProductId().equals(comment.productId());
            assert entity.getCustomerId().equals(comment.customerId());
            assert entity.getComment().equals(comment.comment());
            assert entity.getScore() == comment.score();
            assert entity.getRegDate().equals(comment.regDate());
            assert entity.getRegDateTime().equals(comment.regDateTime());
        }
    }
}
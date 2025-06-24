package com.product.adapter.out.persistence.jpa;

import com.product.IntegrationTestSupport;
import com.product.adapter.out.persistence.jpa.shard1.CommentShard1Adapter;
import com.product.adapter.out.persistence.jpa.shard2.CommentShard2Adapter;
import com.product.application.port.in.command.FindCommentListCommand;
import com.product.domain.model.Comment;
import com.product.infrastructure.util.ShardKeyUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentStorageAdapterTest extends IntegrationTestSupport {

    @Autowired
    CommentStorageAdapter adapter;
    @Autowired
    CommentShard1Adapter shard1Adapter;
    @Autowired
    CommentShard2Adapter shard2Adapter;


    @Nested
    @DisplayName("[register] 리뷰를 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 상품 아이디가 shard1에 해당하면 shard1에 상품 정보를 저장한다.")
        void success1() {
            // given
            Comment comment = getComment(true);

            // when
            adapter.register(comment);
            Comment result = shard1Adapter.findByProductId(
                    FindCommentListCommand.builder()
                        .productId(comment.productId())
                        .page(0)
                        .size(10)
                        .build())
                .getFirst();

            // then
            assert result.productId().equals(comment.productId());
            assert result.comment().equals(comment.comment());
            assert result.customerEmail().equals(comment.customerEmail());
            assert result.regDate().equals(comment.regDate());
            assert result.regDateTime().equals(comment.regDateTime());
        }

        @Test
        @DisplayName("[success] 상품 아이디가 shard2에 해당하면 shard2에 상품 정보를 저장한다.")
        void success2() {
            // given
            Comment comment = getComment(false);

            // when
            adapter.register(comment);
            Comment result = shard2Adapter.findByProductId(
                    FindCommentListCommand.builder()
                        .productId(comment.productId())
                        .page(0)
                        .size(10)
                        .build())
                .getFirst();

            // then
            assert result.productId().equals(comment.productId());
            assert result.comment().equals(comment.comment());
            assert result.customerEmail().equals(comment.customerEmail());
            assert result.regDate().equals(comment.regDate());
            assert result.regDateTime().equals(comment.regDateTime());
        }
    }

    @Nested
    @DisplayName("[findByProductId] 상품 아이디로 리뷰를 조회하는 메소드")
    class Describe_findByProductId {

        @Test
        @DisplayName("[success] 상품 아이디가 shard1에 해당하면 shard1에서 상품 정보를 조회한다.")
        void success1() {
            // given
            Comment comment = getComment(true);
            shard1Adapter.register(comment);

            // when
            Comment result = adapter.findByProductId(
                FindCommentListCommand.builder()
                    .productId(comment.productId())
                    .page(0)
                    .size(10)
                    .build()).getFirst();

            // then
            assert result.id().equals(comment.id());
            assert result.productId().equals(comment.productId());
            assert result.comment().equals(comment.comment());
            assert result.customerEmail().equals(comment.customerEmail());
            assert result.regDate().equals(comment.regDate());
            assert result.regDateTime().equals(comment.regDateTime());
        }

        @Test
        @DisplayName("[success] 상품 아이디가 shard2에 해당하면 shard2에서 상품 정보를 조회한다.")
        void success2() {
            // given
            Comment comment = getComment(false);
            shard2Adapter.register(comment);

            // when
            Comment result = adapter.findByProductId(
                FindCommentListCommand.builder()
                    .productId(comment.productId())
                    .page(0)
                    .size(10)
                    .build()).getFirst();

            // then
            assert result.id().equals(comment.id());
            assert result.productId().equals(comment.productId());
            assert result.comment().equals(comment.comment());
            assert result.customerEmail().equals(comment.customerEmail());
            assert result.regDate().equals(comment.regDate());
            assert result.regDateTime().equals(comment.regDateTime());
        }
    }

    @Nested
    @DisplayName("[deleteByProductId] 상품 ID로 리뷰를 삭제하는 API")
    class Describe_deleteByproductId {

        @Test
        @DisplayName("[success] 상품 아이디가 shard1에 해당하면 shard1에서 상품 정보를 삭제한다.")
        void success() {
            // given
            Comment comment = getComment(true);
            shard1Adapter.register(comment);
            assert shard1Adapter.findByProductId(
                FindCommentListCommand.builder()
                    .productId(comment.productId())
                    .page(0)
                    .size(10)
                    .build()).size() == 1;

            // when
            adapter.deleteByProductId(comment.productId());
            List<Comment> result = shard1Adapter.findByProductId(
                FindCommentListCommand.builder()
                    .productId(comment.productId())
                    .page(0)
                    .size(10)
                    .build());

            // then
            assert result.isEmpty();
        }

        @Test
        @DisplayName("[success] 상품 아이디가 shard2에 해당하면 shard2에서 상품 정보를 삭제한다.")
        void success2() {
            // given
            Comment comment = getComment(false);
            shard2Adapter.register(comment);
            assert shard2Adapter.findByProductId(FindCommentListCommand.builder()
                .productId(comment.productId())
                .page(0)
                .size(10)
                .build()).size() == 1;

            // when
            adapter.deleteByProductId(comment.productId());
            List<Comment> result = shard1Adapter.findByProductId(
                FindCommentListCommand.builder()
                    .productId(comment.productId())
                    .page(0)
                    .size(10)
                    .build());

            // then
            assert result.isEmpty();
        }
    }

    private Comment getComment(boolean isShard1) {
        long productId;
        if (isShard1) {
            while (true) {
                productId = snowflakeGenerator.nextId();
                if (ShardKeyUtil.isShard1(productId)) {
                    break;
                }
            }
        } else {
            while (true) {
                productId = snowflakeGenerator.nextId();
                if (!ShardKeyUtil.isShard1(productId)) {
                    break;
                }
            }
        }

        return Comment.builder()
            .id(snowflakeGenerator.nextId())
            .productId(productId)
            .comment("리뷰 내용")
            .customerEmail("작성자")
            .regDate(LocalDate.of(2025, 1, 1))
            .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
            .build();
    }
}
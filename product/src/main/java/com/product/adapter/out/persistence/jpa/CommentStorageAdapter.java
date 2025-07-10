package com.product.adapter.out.persistence.jpa;

import com.product.adapter.out.persistence.jpa.shard1.CommentShard1Adapter;
import com.product.adapter.out.persistence.jpa.shard2.CommentShard2Adapter;
import com.product.application.port.in.command.FindCommentListCommand;
import com.product.application.port.in.command.RegisterCommentCommand;
import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Comment;
import com.product.infrastructure.util.ShardKeyUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
class CommentStorageAdapter implements CommentStoragePort {

    private final CommentShard1Adapter shard1Adapter;
    private final CommentShard2Adapter shard2Adapter;

    @Override
    public List<Comment> findByProductId(FindCommentListCommand command) {
        return ShardKeyUtil.isShard1(command.productId()) ?
            shard1Adapter.findByProductId(command) : shard2Adapter.findByProductId(command);
    }

    @Override
    public void register(Comment comment) {
        if (ShardKeyUtil.isShard1(comment.productId())) {
            shard1Adapter.register(comment);
        } else {
            shard2Adapter.register(comment);
        }
    }

    @Override
    public boolean existsByCustomerIdAndProductId(RegisterCommentCommand command) {
        return ShardKeyUtil.isShard1(command.productId()) ?
            shard1Adapter.existsByCustomerIdAndProductId(command)
            : shard2Adapter.existsByCustomerIdAndProductId(command);
    }

    @Override
    public void deleteByProductId(Long productId) {
        if (ShardKeyUtil.isShard1(productId)) {
            shard1Adapter.deleteByProductId(productId);
        } else {
            shard2Adapter.deleteByProductId(productId);
        }
    }
}

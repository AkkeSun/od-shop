package com.product.adapter.out.persistence.jpa;

import com.product.adapter.out.persistence.jpa.shard1.CommentShard1Adapter;
import com.product.adapter.out.persistence.jpa.shard2.CommentShard2Adapter;
import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Comment;
import com.product.infrastructure.util.ShardKeyUtil;
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
    public void register(Comment comment) {
        if (ShardKeyUtil.isShard1(comment.productId())) {
            shard1Adapter.register(comment);
        } else {
            shard2Adapter.register(comment);
        }
    }
}

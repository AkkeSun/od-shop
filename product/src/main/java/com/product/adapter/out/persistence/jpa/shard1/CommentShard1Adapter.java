package com.product.adapter.out.persistence.jpa.shard1;

import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "primaryTransactionManager")
public class CommentShard1Adapter implements CommentStoragePort {

    private final CommentShard1Repository repository;

    @Override
    public void register(Comment comment) {
        repository.save(CommentShard1Entity.of(comment));
    }
}

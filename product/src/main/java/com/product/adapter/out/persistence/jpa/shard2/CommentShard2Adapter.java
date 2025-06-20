package com.product.adapter.out.persistence.jpa.shard2;

import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "secondaryTransactionManager")
public class CommentShard2Adapter implements CommentStoragePort {

    private final CommentShard2Repository repository;

    @Override
    public void register(Comment comment) {
        repository.save(CommentShard2Entity.of(comment));
    }
}

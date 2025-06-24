package com.product.adapter.out.persistence.jpa.shard1;

import com.product.application.port.in.command.FindCommentListCommand;
import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Comment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "primaryTransactionManager")
public class CommentShard1Adapter implements CommentStoragePort {

    private final CommentShard1Repository repository;

    @Override
    public List<Comment> findByProductId(FindCommentListCommand command) {
        List<CommentShard1Entity> entities = repository.findByProductId(command.productId(),
            PageRequest.of(command.page(), command.size()));

        return entities.stream()
            .map(CommentShard1Entity::toDomain)
            .toList();
    }

    @Override
    public void register(Comment comment) {
        repository.save(CommentShard1Entity.of(comment));
    }

    @Override
    public void deleteByProductId(Long productId) {
        repository.deleteByProductId(productId);
    }
}

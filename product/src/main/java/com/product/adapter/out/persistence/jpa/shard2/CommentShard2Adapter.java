package com.product.adapter.out.persistence.jpa.shard2;

import com.product.application.port.in.command.FindCommentListCommand;
import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Comment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "secondaryTransactionManager")
public class CommentShard2Adapter implements CommentStoragePort {

    private final CommentShard2Repository repository;

    @Override
    public List<Comment> findByProductId(FindCommentListCommand command) {
        List<CommentShard2Entity> entities = repository.findByProductId(command.productId(),
            command.pageable());

        return entities.stream()
            .map(CommentShard2Entity::toDomain)
            .toList();
    }

    @Override
    public void register(Comment comment) {
        repository.save(CommentShard2Entity.of(comment));
    }

    @Override
    public void deleteByProductId(Long productId) {
        repository.deleteByProductId(productId);
    }
}

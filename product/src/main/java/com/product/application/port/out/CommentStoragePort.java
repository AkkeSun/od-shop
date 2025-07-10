package com.product.application.port.out;

import com.product.application.port.in.command.FindCommentListCommand;
import com.product.application.port.in.command.RegisterCommentCommand;
import com.product.domain.model.Comment;
import java.util.List;

public interface CommentStoragePort {

    List<Comment> findByProductId(FindCommentListCommand command);

    void register(Comment comment);

    boolean existsByCustomerIdAndProductId(RegisterCommentCommand command);

    void deleteByProductId(Long productId);
}

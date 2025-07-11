package com.product.fakeClass;

import com.product.application.port.in.command.FindCommentListCommand;
import com.product.application.port.in.command.RegisterCommentCommand;
import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Comment;
import java.util.ArrayList;
import java.util.List;

public class FakeCommentStoragePort implements CommentStoragePort {

    public List<Comment> database = new ArrayList<>();

    @Override
    public List<Comment> findByProductId(FindCommentListCommand command) {
        return database.stream().filter(comment -> command.productId().equals(command.productId()))
            .limit(command.pageable().getPageSize())
            .toList();
    }

    @Override
    public void register(Comment comment) {
        database.add(comment);
    }

    @Override
    public boolean existsByCustomerIdAndProductId(RegisterCommentCommand command) {
        return !database.stream()
            .filter(data -> data.customerId().equals(command.account().id()))
            .filter(data -> data.productId().equals(command.productId()))
            .toList().isEmpty();
    }

    @Override
    public void deleteByProductId(Long productId) {
        database.removeIf(comment -> comment.productId().equals(productId));
    }
}

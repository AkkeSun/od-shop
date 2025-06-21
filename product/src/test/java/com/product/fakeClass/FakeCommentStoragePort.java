package com.product.fakeClass;

import com.product.application.port.in.command.FindCommentListCommand;
import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Comment;
import java.util.ArrayList;
import java.util.List;

public class FakeCommentStoragePort implements CommentStoragePort {

    public List<Comment> database = new ArrayList<>();

    @Override
    public List<Comment> findByProductId(FindCommentListCommand command) {
        return database.stream().filter(comment -> command.productId().equals(command.productId()))
            .limit(command.size())
            .toList();
    }

    @Override
    public void register(Comment comment) {
        database.add(comment);
    }
}

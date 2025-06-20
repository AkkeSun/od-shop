package com.product.application.port.out;

import com.product.domain.model.Comment;

public interface CommentStoragePort {

    void register(Comment comment);
}

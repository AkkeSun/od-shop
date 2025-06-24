package com.product.application.service.find_comment_list;

import com.product.application.port.in.FindCommentListUseCase;
import com.product.application.port.in.command.FindCommentListCommand;
import com.product.application.port.out.CommentStoragePort;
import com.product.domain.model.Comment;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindCommentListService implements FindCommentListUseCase {

    private final CommentStoragePort commentStoragePort;

    @NewSpan
    @Override
    public FindCommentListServiceResponse findCommentList(FindCommentListCommand command) {
        List<Comment> comments = commentStoragePort.findByProductId(command);
        return FindCommentListServiceResponse.of(comments, command);
    }
}

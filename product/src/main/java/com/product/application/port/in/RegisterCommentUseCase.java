package com.product.application.port.in;

import com.product.application.port.in.command.RegisterCommentCommand;
import com.product.application.service.register_comment.RegisterCommentServiceResponse;

public interface RegisterCommentUseCase {

    RegisterCommentServiceResponse registerComment(RegisterCommentCommand command);
}

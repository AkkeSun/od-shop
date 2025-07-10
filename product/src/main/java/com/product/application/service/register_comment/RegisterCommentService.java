package com.product.application.service.register_comment;

import static com.product.infrastructure.exception.ErrorCode.ACCESS_DENIED;
import static com.product.infrastructure.exception.ErrorCode.Business_SAVED_REVIEW_INFO;

import com.product.application.port.in.RegisterCommentUseCase;
import com.product.application.port.in.command.RegisterCommentCommand;
import com.product.application.port.out.CommentStoragePort;
import com.product.application.port.out.OrderClientPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Comment;
import com.product.domain.model.Product;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.CustomBusinessException;
import com.product.infrastructure.util.SnowflakeGenerator;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterCommentService implements RegisterCommentUseCase {

    private final SnowflakeGenerator snowflakeGenerator;
    private final OrderClientPort orderClientPort;
    private final ProductStoragePort productStoragePort;
    private final CommentStoragePort commentStoragePort;

    @NewSpan
    @Override
    public RegisterCommentServiceResponse registerComment(RegisterCommentCommand command) {
        Product product = productStoragePort.findByIdAndDeleteYn(command.productId(), "N");
        if (!orderClientPort.isOrderValid(product.getId(), command.account().id())) {
            throw new CustomAuthorizationException(ACCESS_DENIED);
        }
        if (commentStoragePort.existsByCustomerIdAndProductId(command)) {
            throw new CustomBusinessException(Business_SAVED_REVIEW_INFO);
        }
        commentStoragePort.register(Comment.of(command, snowflakeGenerator.nextId()));
        return RegisterCommentServiceResponse.ofSuccess();
    }
}

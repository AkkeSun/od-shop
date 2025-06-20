package com.product.adapter.in.controller.register_comment;

import com.product.application.port.in.RegisterCommentUseCase;
import com.product.application.service.register_comment.RegisterCommentServiceResponse;
import com.product.domain.model.Account;
import com.product.infrastructure.resolver.LoginAccount;
import com.product.infrastructure.response.ApiResponse;
import com.product.infrastructure.validation.groups.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterCommentController {

    private final RegisterCommentUseCase registerCommentUseCase;

    @PostMapping("/products/{productId}/comments")
    ApiResponse<RegisterCommentResponse> registerComment(
        @Validated(ValidationSequence.class) @RequestBody RegisterCommentRequest request,
        @LoginAccount Account account,
        @PathVariable Long productId
    ) {
        RegisterCommentServiceResponse serviceResponse = registerCommentUseCase.registerComment(
            request.toCommand(productId, account));

        return ApiResponse.ok(RegisterCommentResponse.of(serviceResponse));
    }
}

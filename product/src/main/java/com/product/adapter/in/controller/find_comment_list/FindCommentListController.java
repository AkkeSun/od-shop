package com.product.adapter.in.controller.find_comment_list;

import com.product.application.port.in.FindCommentListUseCase;
import com.product.application.service.find_comment_list.FindCommentListServiceResponse;
import com.product.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindCommentListController {

    private final FindCommentListUseCase findCommentListUseCase;

    @GetMapping("/products/{productId}/comments")
    ApiResponse<FindCommentListResponse> findCommentList(
        FindCommentListRequest request, @PathVariable Long productId
    ) {
        FindCommentListServiceResponse serviceResponse = findCommentListUseCase.findCommentList(
            request.toCommand(productId));

        return ApiResponse.ok(FindCommentListResponse.of(serviceResponse));
    }
}

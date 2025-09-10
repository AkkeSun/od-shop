package com.product.adapter.in.controller.find_review_list;

import com.product.application.port.in.FindReviewListUseCase;
import com.product.application.service.find_review_list.FindReviewListServiceResponse;
import com.product.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindReviewListController {

    private final FindReviewListUseCase useCase;

    @GetMapping("/products/{productId}/reviews")
    ApiResponse<FindReviewListResponse> findReviewList(
        FindReviewListRequest request,
        @PathVariable Long productId
    ) {
        FindReviewListServiceResponse serviceResponse = useCase.findReviewList(
            request.toCommand(productId));

        return ApiResponse.ok(FindReviewListResponse.of(serviceResponse));
    }
}

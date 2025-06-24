package com.product.adapter.in.controller.find_product_list;

import com.product.application.port.in.FindProductListUseCase;
import com.product.application.service.find_product_list.FindProductListServiceResponse;
import com.product.infrastructure.response.ApiResponse;
import com.product.infrastructure.validation.groups.ValidationSequence;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindProductListController {

    private final FindProductListUseCase findProductListUseCase;

    @GetMapping("/products")
    ApiResponse<List<FindProductListResponse>> findProductList(
        @Validated(ValidationSequence.class) FindProductListRequest request) {

        List<FindProductListServiceResponse> serviceResponses = findProductListUseCase
            .findProductList(request.toCommand());
        return ApiResponse.ok(serviceResponses.stream()
            .map(FindProductListResponse::of)
            .toList());
    }
}

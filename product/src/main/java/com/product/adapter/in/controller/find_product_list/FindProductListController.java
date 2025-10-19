package com.product.adapter.in.controller.find_product_list;

import com.common.infrastructure.response.ApiResponse;
import com.common.infrastructure.validation.groups.ValidationSequence;
import com.product.application.port.in.FindProductListUseCase;
import com.product.application.service.find_product_list.FindProductListServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindProductListController {

    private final FindProductListUseCase useCase;

    @GetMapping("/products")
    ApiResponse<FindProductListResponse> findProductList(
        @Validated(ValidationSequence.class) FindProductListRequest request
    ) {
        FindProductListServiceResponse serviceResponse = useCase.findProductList(
            request.toCommand());

        return ApiResponse.ok(FindProductListResponse.of(serviceResponse));
    }
}

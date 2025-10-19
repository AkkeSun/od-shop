package com.product.adapter.in.controller.find_product;

import com.common.infrastructure.response.ApiResponse;
import com.product.application.port.in.FindProductUseCase;
import com.product.application.port.in.command.FindProductCommand;
import com.product.application.service.find_product.FindProductServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindProductController {

    private final FindProductUseCase useCase;

    @GetMapping("/products/{productId}")
    ApiResponse<FindProductResponse> findProduct(@PathVariable Long productId) {
        FindProductServiceResponse serviceResponse = useCase.findProduct(
            FindProductCommand.ofApiCall(productId));

        return ApiResponse.ok(FindProductResponse.of(serviceResponse));
    }
}

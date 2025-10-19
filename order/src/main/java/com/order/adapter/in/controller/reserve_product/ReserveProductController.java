package com.order.adapter.in.controller.reserve_product;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.order.application.port.in.ReserveProductUseCase;
import com.order.application.port.in.command.ReserveProductCommand;
import com.order.application.service.reserve_product.ReserveProductServiceResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class ReserveProductController {

    private final ReserveProductUseCase useCase;

    @PostMapping("/orders/reservation")
    ApiResponse<List<ReserveProductResponse>> reserveProducts(
        @RequestBody @Valid List<ReserveProductRequest> request,
        @LoginAccount LoginAccountInfo loginInfo
    ) {

        List<ReserveProductServiceResponse> serviceResponses = useCase.reservation(
            ReserveProductCommand.builder()
                .accountId(loginInfo.getId())
                .items(request.stream().map(ReserveProductRequest::toCommandItem).toList())
                .build());
        return ApiResponse.ok(serviceResponses.stream()
            .map(ReserveProductResponse::of)
            .toList());
    }
}

package com.order.adapter.in.controller.reserve_product;

import com.order.application.port.in.ReserveProductUseCase;
import com.order.application.port.in.command.ReserveProductCommand;
import com.order.application.service.reserve_product.ReserveProductServiceResponse;
import com.order.domain.model.Account;
import com.order.infrastructure.resolver.LoginAccount;
import com.order.infrastructure.response.ApiResponse;
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
        @LoginAccount Account account
    ) {

        List<ReserveProductServiceResponse> serviceResponses = useCase.reservation(
            ReserveProductCommand.builder()
                .accountId(account.id())
                .items(request.stream().map(ReserveProductRequest::toCommandItem).toList())
                .build());
        return ApiResponse.ok(serviceResponses.stream()
            .map(ReserveProductResponse::of)
            .toList());
    }
}

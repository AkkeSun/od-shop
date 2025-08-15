package com.order.adapter.in.controller.reserve_product;

import com.order.domain.model.Account;
import com.order.infrastructure.resolver.LoginAccount;
import com.order.infrastructure.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
class ReserveProductController {

    @PostMapping("/reservation")
    ApiResponse<ReserveProductResponse> reserveProducts(
        @RequestBody @Valid List<ReserveProductRequest> request,
        @LoginAccount Account account
    ) {
        return ApiResponse.ok(null);
    }
}

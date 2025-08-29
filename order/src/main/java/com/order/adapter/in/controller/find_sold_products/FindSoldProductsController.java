package com.order.adapter.in.controller.find_sold_products;

import com.order.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindSoldProductsController {
    
    @GetMapping("/sold-products")
    ApiResponse<FindSoldProductsResponse> findAll() {

        return ApiResponse.ok(null);
    }
}

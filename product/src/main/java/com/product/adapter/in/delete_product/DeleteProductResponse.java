package com.product.adapter.in.delete_product;

import com.product.application.service.delete_product.DeleteProductServiceResponse;
import lombok.Builder;

@Builder
record DeleteProductResponse(
    Boolean result
) {

    static DeleteProductResponse of(DeleteProductServiceResponse serviceResponse) {
        return DeleteProductResponse.builder()
            .result(serviceResponse.result())
            .build();
    }
}

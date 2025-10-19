package com.product.application.port.in;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.product.application.service.delete_product.DeleteProductServiceResponse;

public interface DeleteProductUseCase {

    DeleteProductServiceResponse deleteProduct(Long productId, LoginAccountInfo loginInfo);
}

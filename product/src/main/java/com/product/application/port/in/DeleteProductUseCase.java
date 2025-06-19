package com.product.application.port.in;

import com.product.application.service.delete_product.DeleteProductServiceResponse;
import com.product.domain.model.Account;

public interface DeleteProductUseCase {
    DeleteProductServiceResponse deleteProduct(Long productId, Account account);
}

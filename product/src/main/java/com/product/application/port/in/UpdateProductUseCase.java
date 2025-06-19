package com.product.application.port.in;

import com.product.application.port.in.command.UpdateProductCommand;
import com.product.application.service.update_product.UpdateProductServiceResponse;

public interface UpdateProductUseCase {

    UpdateProductServiceResponse updateProduct(UpdateProductCommand command);
}

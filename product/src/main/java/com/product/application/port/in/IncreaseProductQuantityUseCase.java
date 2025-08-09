package com.product.application.port.in;

import com.product.application.port.in.command.IncreaseProductQuantityCommand;
import com.product.application.service.Increase_product_quantity.IncreaseProductQuantityServiceResponse;

public interface IncreaseProductQuantityUseCase {

    IncreaseProductQuantityServiceResponse update(IncreaseProductQuantityCommand command);
}

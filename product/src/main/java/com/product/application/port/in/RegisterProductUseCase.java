package com.product.application.port.in;

import com.product.application.port.in.command.RegisterProductCommand;
import com.product.application.service.register_product.RegisterProductServiceResponse;

public interface RegisterProductUseCase {

    RegisterProductServiceResponse registerProduct(RegisterProductCommand command);
}

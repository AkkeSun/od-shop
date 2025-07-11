package com.product.application.port.in;

import com.product.application.port.in.command.FindProductCommand;
import com.product.application.service.find_product.FindProductServiceResponse;

public interface FindProductUseCase {

    FindProductServiceResponse findProduct(FindProductCommand command);
}

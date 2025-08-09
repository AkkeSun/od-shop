package com.product.application.port.in;

import com.product.application.port.in.command.ReserveProductCommand;
import com.product.application.service.reserve_product.ReserveProductServiceResponse;

public interface ReserveProductUseCase {

    ReserveProductServiceResponse reserve(ReserveProductCommand command);
}

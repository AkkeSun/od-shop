package com.order.applicatoin.port.in;

import com.order.applicatoin.port.in.command.ReserveProductCommand;
import com.order.applicatoin.service.reserve_product.ReserveProductServiceResponse;

public interface ReserveProductUseCase {

    ReserveProductServiceResponse reservation(ReserveProductCommand command);
}

package com.order.applicatoin.port.in;

import com.order.applicatoin.port.in.command.RegisterOrderCommand;
import com.order.applicatoin.service.register_order.RegisterOrderServiceResponse;

public interface RegisterOrderUseCase {

    RegisterOrderServiceResponse register(RegisterOrderCommand command);
}

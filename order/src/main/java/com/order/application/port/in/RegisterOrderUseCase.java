package com.order.application.port.in;

import com.order.application.port.in.command.RegisterOrderCommand;
import com.order.application.service.register_order.RegisterOrderServiceResponse;

public interface RegisterOrderUseCase {

    RegisterOrderServiceResponse register(RegisterOrderCommand command);
}

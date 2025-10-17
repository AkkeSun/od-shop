package com.order.application.port.in;

import com.order.application.port.in.command.CancelOrderCommand;
import com.order.application.service.cancel_order.CancelOrderServiceResponse;

public interface CancelOrderUseCase {

    CancelOrderServiceResponse cancel(CancelOrderCommand command);
}

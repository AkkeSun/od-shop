package com.order.applicatoin.port.in;

import com.order.applicatoin.port.in.command.CancelOrderCommand;
import com.order.applicatoin.service.cancel_order.CancelOrderServiceResponse;

public interface CancelOrderUseCase {

    CancelOrderServiceResponse cancel(CancelOrderCommand command);
}

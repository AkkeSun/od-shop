package com.order.applicatoin.port.out;

import com.order.applicatoin.port.in.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.applicatoin.port.in.command.ReserveProductCommand.ReserveProductCommandItem;

public interface ProductClientPort {

    Long reserveProduct(ReserveProductCommandItem command, Long accountId);

    void confirmReserve(RegisterOrderCommandItem command);
}

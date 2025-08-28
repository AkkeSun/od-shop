package com.order.applicatoin.port.out;

import com.order.applicatoin.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.applicatoin.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.domain.model.OrderProduct;
import com.order.domain.model.Product;

public interface ProductClientPort {

    Long reserveProduct(ReserveProductCommandItem command, Long accountId);

    OrderProduct confirmReserve(RegisterOrderCommandItem command);

    Product findProduct(Long productId);
}

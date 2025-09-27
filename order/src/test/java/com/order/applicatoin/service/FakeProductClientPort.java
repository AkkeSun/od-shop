package com.order.applicatoin.service;

import com.order.applicatoin.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.applicatoin.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.applicatoin.port.out.ProductClientPort;
import com.order.domain.model.OrderProduct;
import com.order.domain.model.Product;
import java.util.HashMap;
import java.util.Map;

public class FakeProductClientPort implements ProductClientPort {

    public Map<Long, Product> mapDatabase = new HashMap<>();

    @Override
    public Long reserveProduct(ReserveProductCommandItem command, Long accountId) {
        return 0L;
    }

    @Override
    public OrderProduct confirmReserve(RegisterOrderCommandItem command) {
        return null;
    }

    @Override
    public Product findProduct(Long productId) {
        return mapDatabase.get(productId);
    }
}

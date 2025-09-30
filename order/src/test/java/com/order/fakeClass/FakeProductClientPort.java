package com.order.fakeClass;

import com.order.applicatoin.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.applicatoin.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.applicatoin.port.out.ProductClientPort;
import com.order.domain.model.OrderProduct;
import com.order.domain.model.Product;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
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
        if (command.reserveId().equals(99L)) {
            throw new StatusRuntimeException(Status.NOT_FOUND);
        }
        return OrderProduct.builder()
            .productId(command.productId())
            .sellerId(40L)
            .buyQuantity(5)
            .buyStatus("ORDER")
            .build();
    }

    @Override
    public Product findProduct(Long productId) {
        return mapDatabase.get(productId);
    }
}

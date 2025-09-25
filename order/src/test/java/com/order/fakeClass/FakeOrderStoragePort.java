package com.order.fakeClass;

import com.order.applicatoin.port.in.command.ExistsCustomerOrderCommand;
import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.applicatoin.port.in.command.FindOrderProductIdsCommand;
import com.order.applicatoin.port.in.command.FindSoldProductsCommand;
import com.order.applicatoin.port.out.OrderStoragePort;
import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class FakeOrderStoragePort implements OrderStoragePort {

    public List<Order> database = new ArrayList<>();
    public Long id = 0L;

    @Override
    public Order register(Order order) {
        Long orderNumber = ++id;
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProduct product : order.products()) {
            orderProducts.add(OrderProduct.builder()
                .orderNumber(orderNumber)
                .productId(product.getProductId())
                .sellerId(product.getSellerId())
                .buyQuantity(product.getBuyQuantity())
                .buyStatus(product.getBuyStatus())
                .build());
        }
        Order savedOrder = Order.builder()
            .orderNumber(orderNumber)
            .customerId(order.customerId())
            .totalPrice(order.totalPrice())
            .receiverName(order.receiverName())
            .receiverTel(order.receiverTel())
            .receiverAddress(order.receiverAddress())
            .products(orderProducts)
            .regDateTime(order.regDateTime())
            .build();
        database.add(savedOrder);
        return savedOrder;
    }

    @Override
    public void cancel(Order order) {
        database = database.stream()
            .filter(o -> !o.orderNumber().equals(order.orderNumber()))
            .collect(Collectors.toList());
        database.add(order);
    }

    @Override
    public Page<Order> findByCustomerId(FindCustomerOrdersCommand command) {
        return null;
    }

    @Override
    public Page<OrderProduct> findSoldProducts(FindSoldProductsCommand command) {
        return null;
    }

    @Override
    public Order findById(Long id) {
        return database.stream()
            .filter(order -> order.orderNumber().equals(id))
            .toList().getFirst();
    }

    @Override
    public boolean existsCustomerIdAndProductId(ExistsCustomerOrderCommand command) {
        return !database.stream()
            .filter(order -> order.customerId().equals(command.customerId()))
            .toList().isEmpty();
    }

    @Override
    public List<Long> findOrderProductIds(FindOrderProductIdsCommand command) {
        List<Long> response = new ArrayList<>();
        List<Order> list = database.stream()
            .filter(order -> order.customerId().equals(command.customerId()))
            .toList();

        for (Order order : list) {
            order.products().forEach(product -> response.add(product.getProductId()));
        }
        return response;
    }
}

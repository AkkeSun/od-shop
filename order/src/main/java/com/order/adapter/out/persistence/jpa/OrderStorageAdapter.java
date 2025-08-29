package com.order.adapter.out.persistence.jpa;

import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.applicatoin.port.in.command.FindSoldProductsCommand;
import com.order.applicatoin.port.out.OrderStoragePort;
import com.order.domain.model.Order;
import com.order.infrastructure.exception.CustomNotFoundException;
import com.order.infrastructure.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
class OrderStorageAdapter implements OrderStoragePort {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    public Order register(Order order) {
        OrderEntity entity = orderRepository.save(OrderEntity.of(order));
        List<OrderProductEntity> productEntities = order.products().stream()
            .map(product -> OrderProductEntity.of(product, entity))
            .toList();

        orderProductRepository.saveAll(productEntities);
        return entity.toDomain(order.products());
    }

    @Override
    public Page<Order> findByCustomerId(FindCustomerOrdersCommand command) {
        Pageable pageable = PageRequest.of(command.page(), command.size());
        Page<OrderEntity> page = orderRepository.findByCustomerId(command.customerId(), pageable);

        List<Order> orders = page.getContent().stream()
            .map(OrderEntity::toDomain)
            .toList();

        if (orders.isEmpty()) {
            throw new CustomNotFoundException(ErrorCode.DoesNotExist_Order);
        }
        return new PageImpl<>(orders, pageable, orders.size());
    }

    @Override
    public Page<Order> findSoldProducts(FindSoldProductsCommand command) {
        Pageable pageable = PageRequest.of(command.page(), command.size());
        Page<OrderEntity> page = command.isProductIdSearch() ?
            orderRepository.findBySellerIdAndProductNumber(
                command.sellerId(), command.query(), pageable) :
            orderRepository.findBySellerIdAndCustomerId(
                command.sellerId(), command.query(), pageable);

        List<Order> orders = page.getContent().stream()
            .map(OrderEntity::toDomain)
            .toList();
        if (orders.isEmpty()) {
            throw new CustomNotFoundException(ErrorCode.DoesNotExist_Order);
        }
        return new PageImpl<>(orders, pageable, orders.size());
    }
}

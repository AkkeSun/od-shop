package com.order.adapter.out.clinet.persistence.jpa;

import com.order.applicatoin.port.out.OrderStoragePort;
import com.order.domain.model.Order;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
        List<OrderProductEntity> productEntities = order.productIds().stream()
            .map(productId -> OrderProductEntity.of(entity.getOrderNumber(), productId))
            .toList();

        orderProductRepository.saveAll(productEntities);
        return entity.toDomain(order.productIds());
    }
}

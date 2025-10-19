package com.order.adapter.out.persistence.jpa;

import com.common.infrastructure.exception.CustomNotFoundException;
import com.common.infrastructure.exception.ErrorCode;
import com.order.application.port.in.command.ExistsCustomerOrderCommand;
import com.order.application.port.in.command.FindCustomerOrdersCommand;
import com.order.application.port.in.command.FindOrderProductIdsCommand;
import com.order.application.port.in.command.FindSoldProductsCommand;
import com.order.application.port.out.OrderStoragePort;
import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
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
    private final OrderHistoryRepository orderHistoryRepository;
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
    public void cancel(Order order) {
        orderProductRepository.saveAll(order.products().stream()
            .map(OrderProductEntity::of)
            .toList());
        orderHistoryRepository.save(OrderHistoryEntity.ofCancel(order));
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
    public Page<OrderProduct> findSoldProducts(FindSoldProductsCommand command) {
        Pageable pageable = PageRequest.of(command.page(), command.size());

        try {
            return switch (command.searchType()) {
                case "productId" -> orderProductRepository.findBySellerIdAndProductNumber(
                    command.sellerId(), Long.valueOf(command.query()), pageable);
                case "customerId" -> orderProductRepository.findBySellerIdAndCustomerId(
                    command.sellerId(), Long.valueOf(command.query()), pageable);
                case "buyStatus" -> orderProductRepository.findBySellerIdAndBuyStatus(
                    command.sellerId(), command.query(), pageable);
                default -> orderProductRepository.findBySellerId(command.sellerId(), pageable);
            };
        } catch (Exception e) {
            throw new CustomNotFoundException(ErrorCode.DoesNotExist_Order);
        }
    }

    @Override
    public Order findById(Long id) {
        OrderEntity entity = orderRepository.findByOrderNumber(id)
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.DoesNotExist_Order));
        return entity.toDomain();
    }

    @Override
    public boolean existsCustomerIdAndProductId(ExistsCustomerOrderCommand command) {
        return orderRepository.existsByCustomerIdAndOrderProductsProductIdAndOrderProductsBuyStatusIn(
            command.customerId(), command.productId(), List.of("ORDER", "COMPLETE"));
    }

    @Override
    public List<Long> findOrderProductIds(FindOrderProductIdsCommand command) {
        List<Long> productIds = orderRepository.findProductIdsByCustomerIdAndOrderProductsBuyStatusIn(
            command.customerId(), PageRequest.of(0, command.limit()), List.of("ORDER", "COMPLETE"));
        if (productIds.isEmpty()) {
            throw new CustomNotFoundException(ErrorCode.DoesNotExist_Order);
        }
        return productIds;
    }
}

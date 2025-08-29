package com.order.adapter.out.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(""
        + " select order "
        + " from OrderEntity order "
        + " join fetch order.orderProducts "
        + " where order.customerId = :customerId ")
    Page<OrderEntity> findByCustomerId(Long customerId, Pageable pageable);

    @Query(""
        + " select order "
        + " from OrderEntity order "
        + " join fetch order.orderProducts product "
        + " where product.sellerId = :sellerId and order.customerId = :customerId ")
    Page<OrderEntity> findBySellerIdAndCustomerId(Long sellerId, Long customerId,
        Pageable pageable);

    @Query(""
        + " select order "
        + " from OrderEntity order "
        + " join fetch order.orderProducts product "
        + " where product.sellerId = :sellerId and product.productId = :productId ")
    Page<OrderEntity> findBySellerIdAndProductNumber(Long sellerId, Long productId,
        Pageable pageable);
}

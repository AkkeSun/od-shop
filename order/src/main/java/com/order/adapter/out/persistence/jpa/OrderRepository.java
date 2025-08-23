package com.order.adapter.out.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(""
        + " select order "
        + " from OrderEntity order "
        + " join fetch order.orderProducts ororder "
        + " where order.customerId = :customerId ")
    Page<OrderEntity> findByCustomerId(Long customerId, Pageable pageable);
}

package com.order.adapter.out.clinet.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

}

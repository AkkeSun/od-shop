package com.order.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {

}

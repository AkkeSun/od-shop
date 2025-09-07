package com.order.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, Long> {

}

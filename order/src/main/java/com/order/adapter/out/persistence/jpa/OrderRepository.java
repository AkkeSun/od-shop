package com.order.adapter.out.persistence.jpa;

import java.util.List;
import java.util.Optional;
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

    @Query(""
        + " select order "
        + " from OrderEntity order "
        + " join fetch order.orderProducts product "
        + " where product.sellerId = :sellerId and product.buyStatus = :buyStatus ")
    Page<OrderEntity> findBySellerIdAndBuyStatus(Long sellerId, String buyStatus,
        Pageable pageable);

    @Query(""
        + " select order "
        + " from OrderEntity order "
        + " join fetch order.orderProducts product "
        + " where product.sellerId = :sellerId ")
    Page<OrderEntity> findBySellerId(Long sellerId, Pageable pageable);

    @Query(""
        + " select order "
        + " from OrderEntity order "
        + " join fetch order.orderProducts product "
        + " where order.orderNumber = :orderNumber ")
    Optional<OrderEntity> findByOrderNumber(Long orderNumber);

    boolean existsByCustomerIdAndOrderProductsProductIdAndOrderProductsBuyStatusIn(
        Long customerId, Long productId, List<String> statuses);


    @Query("""
          select product.productId
          from OrderEntity order
          join order.orderProducts product
          where order.customerId = :customerId
          and product.buyStatus in :statuses
          order by product.id desc
        """)
    List<Long> findProductIdsByCustomerIdAndOrderProductsBuyStatusIn(Long customerId,
        Pageable pageable, List<String> statuses);
}

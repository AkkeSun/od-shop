package com.order.adapter.out.persistence.jpa;

import com.order.domain.model.OrderProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {

    @Query(""
        + " select  new com.order.domain.model.OrderProduct("
        + "     product.id, product.orderNumber, product.productId, "
        + "     order.customerId, product.sellerId, product.buyQuantity, "
        + "     product.buyStatus, product.regDateTime, product.updateDateTime"
        + " ) "
        + " from OrderProductEntity product "
        + " join OrderEntity order "
        + " on product.orderNumber = order.orderNumber"
        + " where product.sellerId = :sellerId and order.customerId = :customerId ")
    Page<OrderProduct> findBySellerIdAndCustomerId(Long sellerId, Long customerId,
        Pageable pageable);

    @Query(""
        + " select  new com.order.domain.model.OrderProduct("
        + "     product.id, product.orderNumber, product.productId, "
        + "     order.customerId, product.sellerId, product.buyQuantity, "
        + "     product.buyStatus, product.regDateTime, product.updateDateTime"
        + " ) "
        + " from OrderProductEntity product "
        + " join OrderEntity order "
        + " on product.orderNumber = order.orderNumber"
        + " where product.sellerId = :sellerId and product.productId = :productId ")
    Page<OrderProduct> findBySellerIdAndProductNumber(Long sellerId, Long productId,
        Pageable pageable);

    @Query(""
        + " select  new com.order.domain.model.OrderProduct("
        + "     product.id, product.orderNumber, product.productId, "
        + "     order.customerId, product.sellerId, product.buyQuantity, "
        + "     product.buyStatus, product.regDateTime, product.updateDateTime"
        + " ) "
        + " from OrderProductEntity product "
        + " join OrderEntity order "
        + " on product.orderNumber = order.orderNumber"
        + " where product.sellerId = :sellerId and product.buyStatus = :buyStatus ")
    Page<OrderProduct> findBySellerIdAndBuyStatus(Long sellerId, String buyStatus,
        Pageable pageable);

    @Query(""
        + " select  new com.order.domain.model.OrderProduct("
        + "     product.id, product.orderNumber, product.productId, "
        + "     order.customerId, product.sellerId, product.buyQuantity, "
        + "     product.buyStatus, product.regDateTime, product.updateDateTime"
        + " ) "
        + " from OrderProductEntity product "
        + " join OrderEntity order "
        + " on product.orderNumber = order.orderNumber"
        + " where product.sellerId = :sellerId ")
    Page<OrderProduct> findBySellerId(Long sellerId, Pageable pageable);
}

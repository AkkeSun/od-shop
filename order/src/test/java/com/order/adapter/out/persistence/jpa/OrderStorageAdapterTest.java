package com.order.adapter.out.persistence.jpa;

import com.order.adapter.IntegrationTestSupport;
import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

class OrderStorageAdapterTest extends IntegrationTestSupport {

    @Autowired
    OrderStorageAdapter adapter;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;

    @BeforeEach
    void setup() {
        orderProductRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Nested
    @DisplayName("[register] 주문을 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 주문이 정상적으로 등록되는지 확인한다.")
        void success() {
            // given
            OrderProduct product = OrderProduct.builder()
                .productId(20L)
                .sellerId(30L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            Order order = Order.builder()
                .customerId(10L)
                .totalPrice(10000)
                .receiverName("receiverName")
                .receiverTel("receiverTel")
                .receiverAddress("receiverAddress")
                .products(List.of(product))
                .build();

            // when
            adapter.register(order);
            Order savedOrder = adapter.findByCustomerId(FindCustomerOrdersCommand.builder()
                .page(0)
                .size(10)
                .customerId(10L)
                .build()).getContent().getFirst();
            OrderProduct savedProduct = savedOrder.products().getFirst();

            // then
            assert savedOrder.totalPrice() == order.totalPrice();
            assert savedOrder.receiverName().equals(order.receiverName());
            assert savedOrder.receiverTel().equals(order.receiverTel());
            assert savedOrder.receiverAddress().equals(order.receiverAddress());
            assert savedProduct.getProductId().equals(product.getProductId());
            assert savedProduct.getSellerId().equals(product.getSellerId());
            assert savedProduct.getBuyQuantity() == product.getBuyQuantity();
            assert savedProduct.getBuyStatus().equals(product.getBuyStatus());
        }
    }

    @Nested
    @DisplayName("[cancel] 주문을 취소하는 메소드")
    class Describe_cancel {

        @Test
        @DisplayName("[success] 주문이 정상적으로 취소되는지 확인한다")
        void success() {
            // given
            OrderProduct product = OrderProduct.builder()
                .productId(20L)
                .sellerId(30L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            Order order = Order.builder()
                .customerId(10L)
                .totalPrice(10000)
                .receiverName("receiverName")
                .receiverTel("receiverTel")
                .receiverAddress("receiverAddress")
                .products(List.of(product))
                .build();
            adapter.register(order);
            Order savedOrder = adapter.findByCustomerId(FindCustomerOrdersCommand.builder()
                .page(0)
                .size(10)
                .customerId(10L)
                .build()).getContent().getFirst();

            // when
            adapter.cancel(savedOrder);
            Order canceledOrder = adapter.findById(savedOrder.orderNumber());

            // then
            assert canceledOrder.isCanceled();
        }
    }

    @Nested
    @DisplayName("[findByCustomerId] 구매자 아이디로 상품 페이지를 조회하는 메소드")
    class Describe_findByCustomerId {

        @Test
        @DisplayName("[success] 주문이 정상적으로 조회되는지 확인한다")
        void success() {
            // given
            OrderProduct product = OrderProduct.builder()
                .productId(20L)
                .sellerId(30L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            Order order = Order.builder()
                .customerId(10L)
                .totalPrice(10000)
                .receiverName("receiverName")
                .receiverTel("receiverTel")
                .receiverAddress("receiverAddress")
                .products(List.of(product))
                .build();
            adapter.register(order);

            // when
            Page<Order> page = adapter.findByCustomerId(FindCustomerOrdersCommand.builder()
                .page(0)
                .size(10)
                .customerId(10L)
                .build());
            Order savedOrder = page.getContent().getFirst();
            OrderProduct savedProduct = savedOrder.products().getFirst();

            // then
            assert page.getTotalPages() == 1;
            assert page.getTotalElements() == 1;
            assert page.getNumber() == 0;
            assert page.getSize() == 10;
            assert savedOrder.totalPrice() == order.totalPrice();
            assert savedOrder.receiverName().equals(order.receiverName());
            assert savedOrder.receiverTel().equals(order.receiverTel());
            assert savedOrder.receiverAddress().equals(order.receiverAddress());
            assert savedProduct.getProductId().equals(product.getProductId());
            assert savedProduct.getSellerId().equals(product.getSellerId());
            assert savedProduct.getBuyQuantity() == product.getBuyQuantity();
            assert savedProduct.getBuyStatus().equals(product.getBuyStatus());
        }
    }
}
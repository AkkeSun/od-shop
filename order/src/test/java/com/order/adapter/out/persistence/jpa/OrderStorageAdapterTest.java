package com.order.adapter.out.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.order.IntegrationTestSupport;
import com.order.applicatoin.port.in.command.ExistsCustomerOrderCommand;
import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.applicatoin.port.in.command.FindOrderProductIdsCommand;
import com.order.applicatoin.port.in.command.FindSoldProductsCommand;
import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
import com.order.infrastructure.exception.CustomNotFoundException;
import com.order.infrastructure.exception.ErrorCode;
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

        @Test
        @DisplayName("[error] 조회된 상품이 없으면 예외를 응답한다")
        void error() {
            // given
            FindCustomerOrdersCommand command = FindCustomerOrdersCommand.builder()
                .page(0)
                .size(10)
                .customerId(10L)
                .build();

            // when
            CustomNotFoundException result = assertThrows(CustomNotFoundException.class,
                () -> adapter.findByCustomerId(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.DoesNotExist_Order);
        }
    }

    @Nested
    @DisplayName("[findSoldProducts] 판매 상품 목록을 조회하는 메소드")
    class Describe_findSoldProducts {

        @Test
        @DisplayName("[error] 조회된 상품이 없으면 예외를 응답한다")
        void error() {
            // given
            FindSoldProductsCommand command = FindSoldProductsCommand.builder()
                .page(0)
                .size(10)
                .sellerId(10L)
                .build();

            // when
            CustomNotFoundException result = assertThrows(CustomNotFoundException.class,
                () -> adapter.findSoldProducts(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.DoesNotExist_Order);
        }

        @Test
        @DisplayName("[success] 상품코드로 검색하는 경우 해당하는 상품을 응답한다.")
        void success() {
            // given
            OrderProduct product = OrderProduct.builder()
                .productId(20L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            OrderProduct product2 = OrderProduct.builder()
                .productId(21L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            Order order = Order.builder()
                .customerId(10L)
                .totalPrice(10000)
                .receiverName("receiverName")
                .receiverTel("receiverTel")
                .receiverAddress("receiverAddress")
                .products(List.of(product, product2))
                .build();
            adapter.register(order);
            FindSoldProductsCommand command = FindSoldProductsCommand.builder()
                .page(0)
                .size(10)
                .searchType("productId")
                .query("20")
                .sellerId(20L)
                .build();

            // when
            Page<OrderProduct> result = adapter.findSoldProducts(command);

            // then
            assert result.getTotalPages() == 1;
            assert result.getTotalElements() == 1;
            assert result.getNumber() == 0;
            assert result.getSize() == 10;
            assert result.getContent().getFirst().getProductId().equals(20L);
        }

        @Test
        @DisplayName("[success] 구매자 아이디로 검색하는 경우 해당하는 상품을 응답한다.")
        void success2() {
            // given
            OrderProduct product = OrderProduct.builder()
                .productId(20L)
                .sellerId(22L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            OrderProduct product2 = OrderProduct.builder()
                .productId(21L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            Order order = Order.builder()
                .customerId(10L)
                .totalPrice(10000)
                .receiverName("receiverName")
                .receiverTel("receiverTel")
                .receiverAddress("receiverAddress")
                .products(List.of(product, product2))
                .build();
            adapter.register(order);
            FindSoldProductsCommand command = FindSoldProductsCommand.builder()
                .page(0)
                .size(10)
                .searchType("customerId")
                .query("10")
                .sellerId(20L)
                .build();

            // when
            Page<OrderProduct> result = adapter.findSoldProducts(command);

            // then
            assert result.getTotalPages() == 1;
            assert result.getTotalElements() == 1;
            assert result.getNumber() == 0;
            assert result.getSize() == 10;
            assert result.getContent().getFirst().getCustomerId().equals(10L);
        }

        @Test
        @DisplayName("[success] 구매 상태로 검색하는 경우 해당하는 상품을 응답한다.")
        void success3() {
            // given
            OrderProduct product = OrderProduct.builder()
                .productId(20L)
                .sellerId(22L)
                .buyQuantity(10)
                .buyStatus("CANCEL")
                .build();
            OrderProduct product2 = OrderProduct.builder()
                .productId(21L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            Order order = Order.builder()
                .customerId(10L)
                .totalPrice(10000)
                .receiverName("receiverName")
                .receiverTel("receiverTel")
                .receiverAddress("receiverAddress")
                .products(List.of(product, product2))
                .build();
            adapter.register(order);
            FindSoldProductsCommand command = FindSoldProductsCommand.builder()
                .page(0)
                .size(10)
                .searchType("buyStatus")
                .query("ORDER")
                .sellerId(20L)
                .build();

            // when
            Page<OrderProduct> result = adapter.findSoldProducts(command);

            // then
            assert result.getTotalPages() == 1;
            assert result.getTotalElements() == 1;
            assert result.getNumber() == 0;
            assert result.getSize() == 10;
            assert result.getContent().getFirst().getBuyStatus().equals("ORDER");
        }
    }

    @Nested
    @DisplayName("[findById] 아이디로 주문을 조회하는 메소드")
    class Describe_findById {

        @Test
        @DisplayName("[error] 조회된 주문이 없으면 예외를 응답한다")
        void error() {
            // when
            CustomNotFoundException result = assertThrows(CustomNotFoundException.class,
                () -> adapter.findById(-1L));

            // then
            assert result.getErrorCode().equals(ErrorCode.DoesNotExist_Order);
        }

        @Test
        @DisplayName("[success] 조회된 주문이 있으면 주문을 응답한다")
        void success() {
            // given
            OrderProduct product = OrderProduct.builder()
                .productId(20L)
                .sellerId(22L)
                .buyQuantity(10)
                .buyStatus("CANCEL")
                .build();
            OrderProduct product2 = OrderProduct.builder()
                .productId(21L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            Order order = Order.builder()
                .customerId(10L)
                .totalPrice(10000)
                .receiverName("receiverName")
                .receiverTel("receiverTel")
                .receiverAddress("receiverAddress")
                .products(List.of(product, product2))
                .build();
            Order register = adapter.register(order);

            // when
            Order savedOrder = adapter.findById(register.orderNumber());
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
    @DisplayName("[existsCustomerIdAndProductId] 구매자 아이디와 상품 아이디로 유효한 구매 정보가 있는지 확인하는 메소드")
    class Describe_existsCustomerIdAndProductId {

        @Test
        @DisplayName("[success] 조회된 구매가 있으면 true 를 응답한다")
        void success() {
            // given
            OrderProduct product = OrderProduct.builder()
                .productId(20L)
                .sellerId(22L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            OrderProduct product2 = OrderProduct.builder()
                .productId(21L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            Order order = Order.builder()
                .customerId(10L)
                .totalPrice(10000)
                .receiverName("receiverName")
                .receiverTel("receiverTel")
                .receiverAddress("receiverAddress")
                .products(List.of(product, product2))
                .build();
            adapter.register(order);

            // when
            boolean result = adapter.existsCustomerIdAndProductId
                (ExistsCustomerOrderCommand.builder()
                    .customerId(10L)
                    .productId(20L)
                    .build());

            // then
            assert result;
        }

        @Test
        @DisplayName("[success] 조회된 구매가 없으면 false 를 응답한다")
        void success2() {
            // when
            boolean result = adapter.existsCustomerIdAndProductId
                (ExistsCustomerOrderCommand.builder()
                    .customerId(10L)
                    .productId(20L)
                    .build());

            // then
            assert !result;
        }
    }

    @Nested
    @DisplayName("[findOrderProductIds] 판매자의 구매상품 아이디를 조회하는 메소드")
    class Describe_findOrderProductIds {

        @Test
        @DisplayName("[error] 조회된 주문이 없으면 예외를 응답한다")
        void error() {
            // when
            CustomNotFoundException result = assertThrows(CustomNotFoundException.class,
                () -> adapter.findOrderProductIds(
                    FindOrderProductIdsCommand.builder()
                        .customerId(10L)
                        .limit(5)
                    .build()));

            // then
            assert result.getErrorCode().equals(ErrorCode.DoesNotExist_Order);
        }

        @Test
        @DisplayName("[error] 조회된 주문이 있으면 아이디를 응답한다")
        void success() {
            // given
            OrderProduct product = OrderProduct.builder()
                .productId(20L)
                .sellerId(22L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            OrderProduct product2 = OrderProduct.builder()
                .productId(21L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            OrderProduct product3 = OrderProduct.builder()
                .productId(22L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            OrderProduct product4 = OrderProduct.builder()
                .productId(23L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            OrderProduct product5 = OrderProduct.builder()
                .productId(24L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            OrderProduct product6 = OrderProduct.builder()
                .productId(25L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("ORDER")
                .build();
            OrderProduct product7 = OrderProduct.builder()
                .productId(26L)
                .sellerId(20L)
                .buyQuantity(10)
                .buyStatus("CANCEL")
                .build();
            Order order = Order.builder()
                .customerId(10L)
                .totalPrice(10000)
                .receiverName("receiverName")
                .receiverTel("receiverTel")
                .receiverAddress("receiverAddress")
                .products(List.of(product, product2, product3, product4, product5,  product6, product7))
                .build();
            adapter.register(order);

            // when
            List<Long> lists = adapter.findOrderProductIds(
                FindOrderProductIdsCommand.builder()
                    .customerId(10L)
                    .limit(5)
                    .build());

            // then
            assert lists.size() == 5;
            assert lists.contains(25L);
            assert lists.contains(24L);
            assert lists.contains(23L);
            assert lists.contains(22L);
            assert lists.contains(21L);
        }
    }
}
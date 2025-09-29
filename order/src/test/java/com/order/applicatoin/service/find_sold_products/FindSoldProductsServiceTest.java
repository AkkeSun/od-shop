package com.order.applicatoin.service.find_sold_products;

import com.order.applicatoin.port.in.command.FindSoldProductsCommand;
import com.order.applicatoin.service.FakeProductClientPort;
import com.order.applicatoin.service.find_sold_products.FindSoldProductsServiceResponse.FindSoldProductsServiceResponseItem;
import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
import com.order.domain.model.Product;
import com.order.fakeClass.FakeOrderStoragePort;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindSoldProductsServiceTest {

    private final FindSoldProductsService service;
    private final FakeOrderStoragePort orderStoragePort;
    private final FakeProductClientPort productClientPort;

    FindSoldProductsServiceTest() {
        this.orderStoragePort = new FakeOrderStoragePort();
        this.productClientPort = new FakeProductClientPort();
        this.service = new FindSoldProductsService(orderStoragePort, productClientPort);
    }

    @Nested
    @DisplayName("[findAll] 판매 상품을 조회하는 메소드")
    class Describe_findAll {

        @Test
        @DisplayName("[success] 판매 상품을 조회한다.")
        void success() {
            // given
            FindSoldProductsCommand command = FindSoldProductsCommand.builder()
                .sellerId(30L)
                .page(0)
                .size(10)
                .build();
            Product product = Product.builder()
                .id(12L)
                .productName("productName")
                .productImgUrl("productImgUrl")
                .price(10000)
                .buyQuantity(10)
                .build();
            Order order = Order.builder()
                .customerId(15L)
                .totalPrice(100000)
                .receiverName("receiverName")
                .receiverAddress("receiverAddress")
                .products(List.of(OrderProduct.builder()
                    .productId(product.id())
                    .sellerId(30L)
                    .buyQuantity(10)
                    .buyStatus("ORDER")
                    .regDateTime(LocalDateTime.now())
                    .build()))
                .regDateTime(LocalDateTime.now())
                .build();
            productClientPort.mapDatabase.put(product.id(), product);
            Order savedOrder = orderStoragePort.register(order);

            // when
            FindSoldProductsServiceResponse result = service.findAll(command);

            // then
            assert result.pageNumber() == 0;
            assert result.pageSize() == 10;
            assert result.totalPages() == 1;
            assert result.totalElements() == 1;
            FindSoldProductsServiceResponseItem item = result.orderList().getFirst();
            assert item.productName().equals(product.productName());
            assert item.productPrice() == product.price();
            assert item.buyQuantity() == product.buyQuantity();
            assert item.buyStatus().equals("ORDER");
        }
    }
}
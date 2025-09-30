package com.order.applicatoin.service.find_customer_orders;

import static com.order.infrastructure.util.JsonUtil.toJsonString;

import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.applicatoin.service.find_customer_orders.FindCustomerOrdersServiceResponse.FindCustomerOrdersServiceResponseItem;
import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
import com.order.domain.model.Product;
import com.order.fakeClass.FakeOrderStoragePort;
import com.order.fakeClass.FakeProductClientPort;
import com.order.fakeClass.FakeRedisStoragePort;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(OutputCaptureExtension.class)
class FindCustomerOrdersServiceTest {

    private final String key;
    private final FindCustomerOrdersService service;
    private final FakeRedisStoragePort redisStoragePort;
    private final FakeOrderStoragePort orderStoragePort;
    private final FakeProductClientPort productClientPort;

    void setup() {
        redisStoragePort.database.clear();
    }

    FindCustomerOrdersServiceTest() {
        key = "customer-order::%s-%s-%s";
        redisStoragePort = new FakeRedisStoragePort();
        orderStoragePort = new FakeOrderStoragePort();
        productClientPort = new FakeProductClientPort();
        service = new FindCustomerOrdersService(
            redisStoragePort, orderStoragePort, productClientPort);
        ReflectionTestUtils.setField(service, "redisKey", key);
        ReflectionTestUtils.setField(service, "redisTtl", 10000);
    }

    @Nested
    @DisplayName("[findAll] 구매 상품 목록을 조회하는 메소드")
    class Describe_findAll {

        @Test
        @DisplayName("[success] 캐시 데이터가 존재하면 캐시 데이터를 응답한다.")
        void success(CapturedOutput capture) {
            // given
            FindCustomerOrdersCommand command = FindCustomerOrdersCommand.builder()
                .page(0)
                .size(10)
                .customerId(10L)
                .build();
            FindCustomerOrdersServiceResponse response = FindCustomerOrdersServiceResponse.builder()
                .pageNumber(0)
                .pageSize(10)
                .totalPages(1)
                .totalElements(1)
                .orderList(List.of(FindCustomerOrdersServiceResponseItem.builder()
                    .orderNumber(10L)
                    .orderDateTime("orderDateTime")
                    .primaryProductName("primaryProductName")
                    .primaryProductImg("primaryProductImg")
                    .primaryProductBuyStatus("primaryProductBuyStatus")
                    .totalProductCnt(10)
                    .totalPrice(20)
                    .build()))
                .build();
            redisStoragePort.register(
                String.format(key, command.customerId(), command.page(), command.size()),
                toJsonString(response), 100);

            // when
            FindCustomerOrdersServiceResponse result = service.findAll(command);

            // then
            assert !capture.toString().contains("db call");
            assert result.pageNumber() == response.pageNumber();
            assert result.pageSize() == response.pageSize();
            assert result.totalPages() == response.totalPages();
            assert result.totalElements() == response.totalElements();
            FindCustomerOrdersServiceResponseItem responseItem = response.orderList().getFirst();
            FindCustomerOrdersServiceResponseItem resultItem = result.orderList().getFirst();
            assert responseItem.orderNumber().equals(resultItem.orderNumber());
            assert responseItem.orderDateTime().equals(resultItem.orderDateTime());
            assert responseItem.primaryProductName().equals(resultItem.primaryProductName());
            assert responseItem.primaryProductImg().equals(resultItem.primaryProductImg());
            assert responseItem.primaryProductBuyStatus()
                .equals(resultItem.primaryProductBuyStatus());
            assert responseItem.totalProductCnt() == resultItem.totalProductCnt();
            assert responseItem.totalPrice() == resultItem.totalPrice();
        }

        @Test
        @DisplayName("[success] 캐시 데이터가 없으면 DB 및 Product API 를 조회하고 캐시 데이터를 저장한 후 응답한다.")
        void success2(CapturedOutput capture) {
            // given
            FindCustomerOrdersCommand command = FindCustomerOrdersCommand.builder()
                .page(0)
                .size(10)
                .customerId(10L)
                .build();
            Product product = Product.builder()
                .id(12L)
                .productName("productName")
                .productImgUrl("productImgUrl")
                .price(10000)
                .buyQuantity(10)
                .build();
            Order order = Order.builder()
                .customerId(command.customerId())
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
            FindCustomerOrdersServiceResponse result = service.findAll(command);

            // then
            assert capture.toString().contains("db call");
            assert result.pageNumber() == 0;
            assert result.pageSize() == 10;
            assert result.totalPages() == 1;
            assert result.totalElements() == 1;
            FindCustomerOrdersServiceResponseItem resultItem = result.orderList().getFirst();
            assert resultItem.orderNumber().equals(savedOrder.orderNumber());
            assert resultItem.primaryProductName().equals(product.productName());
            assert resultItem.primaryProductImg().equals(product.productImgUrl());
            assert resultItem.primaryProductBuyStatus().equals("ORDER");
            assert resultItem.totalProductCnt() == 1;
            assert resultItem.totalPrice() == order.totalPrice();
            assert redisStoragePort.findData(
                String.format(key, command.customerId(), command.page(), command.size()),
                FindCustomerOrdersServiceResponse.class) != null;
        }
    }
}
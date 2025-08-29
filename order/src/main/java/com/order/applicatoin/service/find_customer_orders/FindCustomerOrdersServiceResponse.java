package com.order.applicatoin.service.find_customer_orders;

import com.order.domain.model.Order;
import com.order.domain.model.Product;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;

@Builder
public record FindCustomerOrdersServiceResponse(
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    List<FindCustomerOrdersServiceResponseItem> orderList
) {

    @Builder
    public record FindCustomerOrdersServiceResponseItem(
        Long orderNumber,
        String orderDateTime,
        String primaryProductName,
        String primaryProductImg,
        String primaryProductBuyStatus,
        long totalProductCnt,
        long totalPrice
    ) {

        static FindCustomerOrdersServiceResponseItem of(Order order, Product product) {
            return FindCustomerOrdersServiceResponseItem.builder()
                .orderNumber(order.orderNumber())
                .orderDateTime(
                    order.regDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")))
                .primaryProductName(product.productName())
                .primaryProductImg(product.productImgUrl())
                .primaryProductBuyStatus(order.products().getFirst().getBuyStatus())
                .totalProductCnt(order.products().size())
                .totalPrice(order.totalPrice())
                .build();
        }
    }
}

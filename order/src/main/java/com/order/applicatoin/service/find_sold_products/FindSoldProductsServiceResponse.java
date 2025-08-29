package com.order.applicatoin.service.find_sold_products;

import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
import com.order.domain.model.Product;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record FindSoldProductsServiceResponse(
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    List<FindSoldProductsServiceResponseItem> orderList
) {


    public static FindSoldProductsServiceResponse of(Page<Order> page,
        List<FindSoldProductsServiceResponseItem> orderList) {
        return FindSoldProductsServiceResponse.builder()
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .totalElements(page.getTotalPages())
            .totalPages(page.getTotalPages())
            .orderList(orderList)
            .build();
    }

    @Builder
    public record FindSoldProductsServiceResponseItem(
        Long orderProductId,
        Long customerId,
        String productName,
        long productPrice,
        long buyQuantity,
        String buyStatus,
        String regDateTime
    ) {

        public static FindSoldProductsServiceResponseItem of(Product product,
            OrderProduct orderProduct, Long customerId) {
            return FindSoldProductsServiceResponseItem.builder()
                .orderProductId(orderProduct.getId())
                .customerId(customerId)
                .productName(product.productName())
                .productPrice(product.price())
                .buyQuantity(orderProduct.getBuyQuantity())
                .buyStatus(orderProduct.getBuyStatus())
                .regDateTime(orderProduct.getRegDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .build();
        }
    }
}

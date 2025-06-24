package com.product.adapter.in.controller.find_product_list;

import com.product.application.service.find_product_list.FindProductListServiceResponse;
import com.product.application.service.find_product_list.FindProductListServiceResponse.FindProductListServiceResponseItem;
import java.util.List;
import lombok.Builder;

@Builder
record FindProductListResponse(
    int page,
    int size,
    int productCount,
    List<FindProductListServiceResponseItem> productList
) {

    static FindProductListResponse of(FindProductListServiceResponse serviceResponse) {
        return FindProductListResponse.builder()
            .page(serviceResponse.page())
            .size(serviceResponse.size())
            .productCount(serviceResponse.productCount())
            .productList(serviceResponse.productList().stream()
                .map(FindProductResponseItem::of)
                .toList()
            )
            .build();
    }

    @Builder
    record FindProductResponseItem(
        Long id,
        String productName,
        String sellerEmail,
        String productImgUrl,
        long price
    ) {

        static FindProductListServiceResponseItem of(FindProductListServiceResponseItem item) {
            return FindProductListServiceResponseItem.builder()
                .id(item.id())
                .productName(item.productName())
                .sellerEmail(item.sellerEmail())
                .productImgUrl(item.productImgUrl())
                .price(item.price())
                .build();
        }
    }
}

package com.product.application.service.find_product_list;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.domain.model.Product;
import java.util.List;
import lombok.Builder;

@Builder
public record FindProductListServiceResponse(
    int page,
    int size,
    int productCount,
    List<FindProductListServiceResponseItem> productList
) {

    public static FindProductListServiceResponse of(List<Product> products,
        FindProductListCommand command) {
        return FindProductListServiceResponse.builder()
            .page(command.page())
            .size(command.size())
            .productCount(products.size())
            .productList(products.stream()
                .map(FindProductListServiceResponseItem::of)
                .toList()
            )
            .build();
    }

    @Builder
    public record FindProductListServiceResponseItem(
        Long id,
        String productName,
        String sellerEmail,
        String productImgUrl,
        long price
    ) {

        public static FindProductListServiceResponseItem of(Product product) {
            return FindProductListServiceResponseItem.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .sellerEmail(product.getSellerEmail())
                .productImgUrl(product.getProductImgUrl())
                .price(product.getPrice())
                .build();
        }
    }
}

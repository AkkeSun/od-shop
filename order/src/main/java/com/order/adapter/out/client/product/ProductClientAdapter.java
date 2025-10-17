package com.order.adapter.out.client.product;

import com.order.application.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.application.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.application.port.out.ProductClientPort;
import com.order.domain.model.OrderProduct;
import com.order.domain.model.Product;
import grpc.product.ConfirmProductReservationRequest;
import grpc.product.ConfirmProductReservationResponse;
import grpc.product.ConfirmProductReservationServiceGrpc.ConfirmProductReservationServiceBlockingStub;
import grpc.product.CreateProductReservationRequest;
import grpc.product.CreateProductReservationResponse;
import grpc.product.CreateProductReservationServiceGrpc.CreateProductReservationServiceBlockingStub;
import grpc.product.FindProductRequest;
import grpc.product.FindProductResponse;
import grpc.product.FindProductServiceGrpc.FindProductServiceBlockingStub;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class ProductClientAdapter implements ProductClientPort {

    @GrpcClient("product")
    private CreateProductReservationServiceBlockingStub reservationService;

    @GrpcClient("product")
    private ConfirmProductReservationServiceBlockingStub confirmReservationService;

    @GrpcClient("product")
    private FindProductServiceBlockingStub findProductService;

    @Override
    public Long reserveProduct(ReserveProductCommandItem command, Long accountId) {
        CreateProductReservationResponse response = reservationService.createReserve(
            CreateProductReservationRequest.newBuilder()
                .setProductId(command.productId())
                .setCustomerId(accountId)
                .setQuantity(command.quantity())
                .build());

        return response.getReserveId();
    }

    @Override
    public OrderProduct confirmReserve(RegisterOrderCommandItem command) {
        ConfirmProductReservationResponse serviceResponse = confirmReservationService
            .confirmReservation(ConfirmProductReservationRequest.newBuilder()
                .setReservationId(command.reserveId())
                .setProductId(command.productId())
                .build());

        return OrderProduct.of(serviceResponse);
    }

    @Override
    public Product findProduct(Long productId) {
        FindProductResponse response = findProductService.findProduct(
            FindProductRequest.newBuilder()
                .setProductId(productId)
                .build());
        
        return Product.builder()
            .id(response.getProductId())
            .sellerEmail(response.getSellerEmail())
            .productName(response.getProductName())
            .productImgUrl(response.getProductImgUrl())
            .descriptionImgUrl(response.getDescriptionImgUrl())
            .price(response.getPrice())
            .buyQuantity(response.getQuantity())
            .category(response.getCategory())
            .build();
    }
}

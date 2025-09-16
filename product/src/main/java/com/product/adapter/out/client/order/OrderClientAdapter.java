package com.product.adapter.out.client.order;

import com.product.application.port.out.OrderClientPort;
import grpc.product.ExistsCustomerProductRequest;
import grpc.product.ExistsCustomerProductServiceGrpc.ExistsCustomerProductServiceBlockingStub;
import grpc.product.FindOrderProductIdsRequest;
import grpc.product.FindOrderProductIdsServiceGrpc.FindOrderProductIdsServiceBlockingStub;
import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
class OrderClientAdapter implements OrderClientPort {

    @GrpcClient("order")
    private ExistsCustomerProductServiceBlockingStub existsCustomerProductService;

    @GrpcClient("order")
    private FindOrderProductIdsServiceBlockingStub findOrderProductIdsService;

    @Override
    public boolean isOrderValid(Long productId, Long accountId) {
        return existsCustomerProductService.exists(
                ExistsCustomerProductRequest.newBuilder()
                    .setProductId(productId)
                    .setCustomerId(accountId)
                    .build())
            .getExists();
    }

    @Override
    public List<Long> findProductIdByAccountId(Long accountId, int limit) {
        return findOrderProductIdsService.findIds(
                FindOrderProductIdsRequest.newBuilder()
                    .setCustomerId(accountId)
                    .setLimit(limit)
                    .build())
            .getProductIdsList();
    }

}

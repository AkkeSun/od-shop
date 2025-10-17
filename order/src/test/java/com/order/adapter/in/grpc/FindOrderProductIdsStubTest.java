package com.order.adapter.in.grpc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.order.applicatoin.port.in.FindOrderProductIdsUseCase;
import grpc.product.FindOrderProductIdsRequest;
import grpc.product.FindOrderProductIdsResponse;
import io.grpc.stub.StreamObserver;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindOrderProductIdsStubTest {

    @Mock
    private FindOrderProductIdsUseCase useCase;

    @Mock
    private StreamObserver<FindOrderProductIdsResponse> responseObserver;

    @InjectMocks
    private FindOrderProductIdsStub stub;

    @Nested
    @DisplayName("[findIds] 고객이 주문한 상품 ID 목록을 조회하는 gRPC 메소드")
    class Describe_findIds {

        @Test
        @DisplayName("[success] 고객의 주문 상품 ID 목록을 반환한다")
        void success() {
            // given
            FindOrderProductIdsRequest request = FindOrderProductIdsRequest.newBuilder()
                .setCustomerId(1L)
                .setLimit(10)
                .build();

            List<Long> mockResponse = List.of(100L, 101L, 102L);

            given(useCase.findOrderProductIds(any())).willReturn(mockResponse);

            // when
            stub.findIds(request, responseObserver);

            // then
            verify(useCase).findOrderProductIds(any());
            verify(responseObserver).onNext(any(FindOrderProductIdsResponse.class));
            verify(responseObserver).onCompleted();
        }

        @Test
        @DisplayName("[success] 주문이 없는 경우 빈 목록을 반환한다")
        void success_emptyList() {
            // given
            FindOrderProductIdsRequest request = FindOrderProductIdsRequest.newBuilder()
                .setCustomerId(1L)
                .setLimit(10)
                .build();

            List<Long> mockResponse = List.of();

            given(useCase.findOrderProductIds(any())).willReturn(mockResponse);

            // when
            stub.findIds(request, responseObserver);

            // then
            verify(useCase).findOrderProductIds(any());
            verify(responseObserver).onNext(any(FindOrderProductIdsResponse.class));
            verify(responseObserver).onCompleted();
        }

        @Test
        @DisplayName("[error] UseCase에서 예외 발생 시 에러 응답을 보낸다")
        void error_useCaseException() {
            // given
            FindOrderProductIdsRequest request = FindOrderProductIdsRequest.newBuilder()
                .setCustomerId(1L)
                .setLimit(10)
                .build();

            given(useCase.findOrderProductIds(any())).willThrow(new RuntimeException("Database error"));

            // when
            stub.findIds(request, responseObserver);

            // then
            verify(useCase).findOrderProductIds(any());
            verify(responseObserver).onError(any());
        }
    }
}

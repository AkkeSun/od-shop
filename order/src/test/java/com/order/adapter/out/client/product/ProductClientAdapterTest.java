package com.order.adapter.out.client.product;

import static io.grpc.Status.INTERNAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.common.infrastructure.exception.CustomGrpcResponseError;
import com.order.IntegrationTestSupport;
import com.order.application.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.application.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.domain.model.OrderProduct;
import com.order.domain.model.Product;
import grpc.product.ConfirmProductReservationResponse;
import grpc.product.ConfirmProductReservationServiceGrpc.ConfirmProductReservationServiceBlockingStub;
import grpc.product.CreateProductReservationResponse;
import grpc.product.CreateProductReservationServiceGrpc.CreateProductReservationServiceBlockingStub;
import grpc.product.FindProductResponse;
import grpc.product.FindProductServiceGrpc.FindProductServiceBlockingStub;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductClientAdapterTest extends IntegrationTestSupport {

    private ProductClientAdapter adapter;

    @Mock
    private CreateProductReservationServiceBlockingStub mockReservationService;

    @Mock
    private ConfirmProductReservationServiceBlockingStub mockConfirmReservationService;

    @Mock
    private FindProductServiceBlockingStub mockFindProductService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        adapter = new ProductClientAdapter();
        injectGrpcClient("reservationService", mockReservationService);
        injectGrpcClient("confirmReservationService", mockConfirmReservationService);
        injectGrpcClient("findProductService", mockFindProductService);
    }

    private void injectGrpcClient(String fieldName, Object mock) throws Exception {
        Field field = ProductClientAdapter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(adapter, mock);
    }

    @Nested
    @DisplayName("t[reserveProduct] 상품을 예약하는 메소드")
    class Describe_reserveProduct {

        @Test
        @DisplayName("[success] 상품 예약에 성공하면 예약 아이디를 응답한다.")
        void success() {
            // given
            when(mockReservationService.createReserve(any()))
                .thenReturn(CreateProductReservationResponse.newBuilder()
                    .setReserveId(10L)
                    .build());
            Long accountId = 1L;
            ReserveProductCommandItem command = ReserveProductCommandItem.builder()
                .productId(10L)
                .quantity(3L)
                .build();

            // when
            Long result = adapter.reserveProduct(command, accountId);

            // then
            assert result.equals(10L);
        }

        @Test
        @DisplayName("[error] 상품 예약에 실패하면 예외를 응답한다.")
        void error() {
            // given
            when(mockReservationService.createReserve(any()))
                .thenThrow(INTERNAL.asRuntimeException());
            Long accountId = 1L;
            ReserveProductCommandItem command = ReserveProductCommandItem.builder()
                .productId(-1L)
                .quantity(3L)
                .build();

            // when & then
            try {
                adapter.reserveProduct(command, accountId);
                assert false;
            } catch (io.grpc.StatusRuntimeException e) {
                assert e.getStatus().getCode() == io.grpc.Status.Code.INTERNAL;
            }
        }
    }

    @Nested
    @DisplayName("[confirmReserve] 상품 예약을 확정하는 메소드")
    class Describe_confirmReserve {

        @Test
        @DisplayName("[success] 상품 예약 확정에 성공하면 주문 상품을 응답한다.")
        void success() {
            // given
            when(mockConfirmReservationService.confirmReservation(any()))
                .thenReturn(ConfirmProductReservationResponse.newBuilder()
                    .setProductId(10L)
                    .setSellerId(20L)
                    .setBuyQuantity(30)
                    .build());
            RegisterOrderCommandItem command = RegisterOrderCommandItem.builder()
                .productId(10L)
                .reserveId(3L)
                .build();

            // when
            OrderProduct result = adapter.confirmReserve(command);

            // then
            assert result.getProductId().equals(10L);
            assert result.getSellerId().equals(20L);
            assert result.getBuyQuantity() == 30;
            assert result.getBuyStatus().equals("ORDER");
        }

        @Test
        @DisplayName("[error] 상품 예약 확정에 실패하면 예외를 응답한다.")
        void error() {
            // given
            when(mockConfirmReservationService.confirmReservation(any()))
                .thenThrow(INTERNAL.asRuntimeException());
            RegisterOrderCommandItem command = RegisterOrderCommandItem.builder()
                .productId(10L)
                .reserveId(3L)
                .build();

            // when & then
            try {
                adapter.confirmReserve(command);
                assert false;
            } catch (io.grpc.StatusRuntimeException e) {
                assert e.getStatus().getCode() == io.grpc.Status.Code.INTERNAL;
            }
        }
    }

    @Nested
    @DisplayName("[findProduct] 상품을 조회하는 메소드")
    class Describe_findProduct {

        @Test
        @DisplayName("[success] 상품 조회에 성공하면 상품 정보를 응답한다.")
        void success() {
            // given
            when(mockFindProductService.findProduct(any()))
                .thenReturn(FindProductResponse.newBuilder()
                    .setProductId(10L)
                    .setSellerEmail("email")
                    .setProductName("productName")
                    .setDescriptionImgUrl("descriptionImgUrl")
                    .setProductImgUrl("productImgUrl")
                    .setPrice(10)
                    .setQuantity(20)
                    .setCategory("category")
                    .build());

            // when
            Product result = adapter.findProduct(10L);

            // then
            assert result.id().equals(10L);
            assert result.sellerEmail().equals("email");
            assert result.productName().equals("productName");
            assert result.descriptionImgUrl().equals("descriptionImgUrl");
            assert result.productImgUrl().equals("productImgUrl");
            assert result.price() == 10;
            assert result.buyQuantity() == 20;
            assert result.category().equals("category");
        }

        @Test
        @DisplayName("[error] 상품 조회에 실패하면 예외를 응답한다.")
        void error() {
            // given
            when(mockFindProductService.findProduct(any()))
                .thenThrow(INTERNAL.asRuntimeException());

            // when & then
            try {
                adapter.findProduct(10L);
                assert false;
            } catch (CustomGrpcResponseError e) {
                assert e.getErrorMessage().equals("INTERNAL");
            }
        }
    }
}
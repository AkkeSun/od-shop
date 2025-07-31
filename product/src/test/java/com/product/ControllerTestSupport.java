package com.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.application.port.in.DeleteProductUseCase;
import com.product.application.port.in.FindProductListUseCase;
import com.product.application.port.in.FindProductUseCase;
import com.product.application.port.in.FindRecommendProductUseCase;
import com.product.application.port.in.FindReviewListUseCase;
import com.product.application.port.in.RegisterProductUseCase;
import com.product.application.port.in.RegisterReviewUseCase;
import com.product.application.port.in.UpdateProductQuantityUseCase;
import com.product.application.port.in.UpdateProductUseCase;
import com.product.application.port.out.AuthorizationStoragePort;
import com.product.domain.model.AuthorizationRule;
import com.product.infrastructure.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtUtil jwtUtil;

    @MockBean
    protected RegisterProductUseCase registerProductUseCase;

    @MockBean
    protected FindProductUseCase findProductUseCase;

    @MockBean
    protected DeleteProductUseCase deleteProductUseCase;

    @MockBean
    protected UpdateProductUseCase updateProductUseCase;

    @MockBean
    protected RegisterReviewUseCase registerReviewUseCase;

    @MockBean
    protected FindReviewListUseCase findReviewListUseCase;

    @MockBean
    protected FindProductListUseCase findProductListUseCase;

    @MockBean
    protected FindRecommendProductUseCase findRecommendProductUseCase;

    @MockBean
    protected UpdateProductQuantityUseCase updateProductQuantityUseCase;

    @Autowired
    private AuthorizationStoragePort authorizationStoragePort;

    @BeforeEach
    void setup() {
        authorizationStoragePort.register(
            AuthorizationRule.builder()
                .sortOrder(1)
                .httpMethod("GET")
                .uriPattern("/products")
                .role("ANONYMOUS")
                .build());
        authorizationStoragePort.register(
            AuthorizationRule.builder()
                .sortOrder(2)
                .httpMethod("GET")
                .uriPattern("/products/**")
                .role("ANONYMOUS")
                .build());
        authorizationStoragePort.register(
            AuthorizationRule.builder()
                .sortOrder(3)
                .httpMethod("GET")
                .uriPattern("/docs/**")
                .role("ANONYMOUS")
                .build());
        authorizationStoragePort.register(
            AuthorizationRule.builder()
                .sortOrder(4)
                .httpMethod("POST")
                .uriPattern("/products")
                .role("ROLE_SELLER")
                .build());
        authorizationStoragePort.register(
            AuthorizationRule.builder()
                .sortOrder(5)
                .httpMethod("PUT")
                .uriPattern("/products/{productId}")
                .role("ROLE_SELLER")
                .build());
        authorizationStoragePort.register(
            AuthorizationRule.builder()
                .sortOrder(6)
                .httpMethod("DELETE")
                .uriPattern("/products/{productId}")
                .role("ROLE_SELLER")
                .build());
        authorizationStoragePort.register(
            AuthorizationRule.builder()
                .sortOrder(7)
                .httpMethod("POST")
                .uriPattern("/products/{productId}/reviews")
                .role("ROLE_CUSTOMER")
                .build());
    }
}

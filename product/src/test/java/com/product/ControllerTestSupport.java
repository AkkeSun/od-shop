package com.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.application.port.in.DeleteProductUseCase;
import com.product.application.port.in.FindCommentListUseCase;
import com.product.application.port.in.FindProductListUseCase;
import com.product.application.port.in.FindProductUseCase;
import com.product.application.port.in.FindRecommendProductUseCase;
import com.product.application.port.in.RegisterCommentUseCase;
import com.product.application.port.in.RegisterProductUseCase;
import com.product.application.port.in.UpdateProductUseCase;
import com.product.infrastructure.config.SecurityConfig;
import com.product.infrastructure.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ActiveProfiles("test")
@Import(SecurityConfig.class)
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
    protected RegisterCommentUseCase registerCommentUseCase;

    @MockBean
    protected FindCommentListUseCase findCommentListUseCase;

    @MockBean
    protected FindProductListUseCase findProductListUseCase;

    @MockBean
    protected FindRecommendProductUseCase findRecommendProductUseCase;
}

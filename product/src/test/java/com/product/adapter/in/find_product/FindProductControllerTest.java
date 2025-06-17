package com.product.adapter.in.find_product;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.application.service.find_product.FindProductServiceResponse;
import com.product.domain.model.Category;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultActions;

class FindProductControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[findProduct] 상품을 조회하는 API")
    class Describe_findProduct {

        @Test
        @WithAnonymousUser
        @DisplayName("[success] API 호출에 성공한다.")
        void success() throws Exception {
            // given
            Long productId = 1L;
            FindProductServiceResponse serviceResponse = FindProductServiceResponse.builder()
                .productId(productId)
                .productName("Test Product")
                .category(Category.AUTOMOTIVE)
                .price(10000)
                .quantity(30)
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .productOption(Set.of("Option1", "Option2"))
                .keywords(Set.of("Keyword1", "Keyword2"))
                .regDateTime("2025-05-01 12:00:00")
                .build();
            when(findProductUseCase.findProduct(productId)).thenReturn(serviceResponse);

            // when
            ResultActions actions = mockMvc.perform(get("/products/{productId}", productId));

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.productId").value(productId))
                .andExpect(jsonPath("$.data.productName").value(serviceResponse.productName()))
                .andExpect(jsonPath("$.data.category").value(serviceResponse.category().name()))
                .andExpect(jsonPath("$.data.price").value(serviceResponse.price()))
                .andExpect(jsonPath("$.data.quantity").value(serviceResponse.quantity()))
                .andExpect(jsonPath("$.data.productImgUrl").value(serviceResponse.productImgUrl()))
                .andExpect(jsonPath("$.data.descriptionImgUrl").value(serviceResponse.descriptionImgUrl()))
                .andExpect(jsonPath("$.data.productOption").isArray())
                .andExpect(jsonPath("$.data.keywords").isArray());
        }
    }
}
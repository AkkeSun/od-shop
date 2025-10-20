package com.product.adapter.in.controller.delete_product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.application.service.delete_product.DeleteProductServiceResponse;
import com.common.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class DeleteProductControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[deleteProduct] 상품을 삭제하는 API")
    class Describe_deleteProduct {

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[success] 판매 권한을 가진 사용자가 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            Long productId = 1L;
            String authorization = "testToken";
            when(deleteProductUseCase.deleteProduct(any(), any()))
                .thenReturn(DeleteProductServiceResponse.builder().result(true).build());

            // when
            ResultActions actions = mockMvc.perform(delete("/products/{productId}", productId)
                .header("Authorization", authorization)
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.result").value(true))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            Long productId = 1L;
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(delete("/products/{productId}", productId)
                .header("Authorization", authorization)
            );

            // then
            actions.andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.httpStatus").value(401))
                .andExpect(jsonPath("$.message").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(
                    ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getCode()))
                .andExpect(jsonPath("$.data.errorMessage").value(
                    ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getMessage()))
                .andDo(print());
        }
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    @DisplayName("[error] 상품 등록 권한이 없는 사용자가 API 를 요청 했을 때 403코드와 에러 메시지를 응답한다.")
    void error2() throws Exception {
        // given
        Long productId = 1L;
        String authorization = "testToken";

        // when
        ResultActions actions = mockMvc.perform(delete("/products/{productId}", productId)
            .header("Authorization", authorization)
        );

        // then
        actions.andExpect(status().isForbidden())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.httpStatus").value(403))
            .andExpect(jsonPath("$.message").value("FORBIDDEN"))
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.errorCode").value(
                ErrorCode.ACCESS_DENIED_BY_SECURITY.getCode()))
            .andExpect(jsonPath("$.data.errorMessage").value(
                ErrorCode.ACCESS_DENIED_BY_SECURITY.getMessage()))
            .andDo(print());
    }
}
package com.product.adapter.in.update_product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.application.service.update_product.UpdateProductServiceResponse;
import com.product.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class UpdateProductControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[updateProduct] 상품을 수정하는 API")
    class Describe_updateProduct {

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[success] 판매 권한을 가진 사용자가 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            UpdateProductRequest request = UpdateProductRequest.builder()
                .productName("Updated Product")
                .build();
            String authorization = "testToken";
            when(updateProductUseCase.updateProduct(any()))
                .thenReturn(UpdateProductServiceResponse.builder()
                    .productId(1L)
                    .productName("Updated Product")
                    .build());

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.productName").value(request.productName()))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            UpdateProductRequest request = UpdateProductRequest.builder().build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
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

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[error] 상품 등록 권한이 없는 사용자가 API 를 요청 했을 때 403코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            UpdateProductRequest request = UpdateProductRequest.builder().build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
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

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 50자 이상으로 입력했을 때400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            UpdateProductRequest request = UpdateProductRequest.builder()
                .productName("test".repeat(13))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("상품명은 50자 이하여야 합니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 50자 이상의 상품 이미지를 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            UpdateProductRequest request = UpdateProductRequest.builder()
                .productImgUrl("test".repeat(13))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("상품 이미지는 50자 이하여야 합니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 50자 이상의 상품 설명 이미지를 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            UpdateProductRequest request = UpdateProductRequest.builder()
                .descriptionImgUrl("test".repeat(13))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("상품 설명 이미지는 50자 이하여야 합니다"))
                .andDo(print());
        }
    }
}
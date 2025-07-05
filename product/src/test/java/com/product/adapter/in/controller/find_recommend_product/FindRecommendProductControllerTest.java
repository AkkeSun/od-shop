package com.product.adapter.in.controller.find_recommend_product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.application.service.find_recommend_product.FindRecommendProductServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class FindRecommendProductControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[findRecommendProduct] 추천 상품 리스트를 조회하는 메소드")
    class Describe_findRecommendProduct {

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 필수값을 모두 입력했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            FindRecommendProductRequest request = FindRecommendProductRequest.builder()
                .searchDate("20250501")
                .build();
            when(findRecommendProductUseCase.findRecommendProductList(any()))
                .thenReturn(FindRecommendProductServiceResponse.builder().build());
            String authorization = "Bearer test-token";

            // when
            ResultActions actions = mockMvc.perform(get("/products/recommendations")
                .param("searchDate", request.getSearchDate())
                .header("Authorization", authorization)
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 조회 권한을 가진 사용자가 검색날짜를 입력하지 않았을 때 400 코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            FindRecommendProductRequest request = FindRecommendProductRequest.builder()
                .searchDate("")
                .build();
            when(findRecommendProductUseCase.findRecommendProductList(any()))
                .thenReturn(FindRecommendProductServiceResponse.builder().build());
            String authorization = "Bearer test-token";

            // when
            ResultActions actions = mockMvc.perform(get("/products/recommendations")
                .param("searchDate", request.getSearchDate())
                .header("Authorization", authorization)
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("검색 날짜는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 조회 권한을 가진 사용자가 유효하지 않은 검색 날짜를 입력했을 때 400 코드와 에러 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            FindRecommendProductRequest request = FindRecommendProductRequest.builder()
                .searchDate("error")
                .build();
            when(findRecommendProductUseCase.findRecommendProductList(any()))
                .thenReturn(FindRecommendProductServiceResponse.builder().build());
            String authorization = "Bearer test-token";

            // when
            ResultActions actions = mockMvc.perform(get("/products/recommendations")
                .param("searchDate", request.getSearchDate())
                .header("Authorization", authorization)
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("유효한 날짜 형식이 아닙니다"))
                .andDo(print());
        }
    }
}
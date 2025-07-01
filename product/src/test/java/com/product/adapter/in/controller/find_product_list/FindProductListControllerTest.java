package com.product.adapter.in.controller.find_product_list;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.application.service.find_product_list.FindProductListServiceResponse;
import com.product.application.service.find_product_list.FindProductListServiceResponse.FindProductListServiceResponseItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultActions;

class FindProductListControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[findProductList] 상품 리스트를 조회하는 API")
    class Describe_findProductList {

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 필수값을 모두 입력하여 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .query("인기있는 신발")
                .category("FASHION")
                .sortType("PRICE_DESC")
                .build();
            FindProductListServiceResponse response = FindProductListServiceResponse.builder()
                .page(0)
                .size(10)
                .productCount(2)
                .productList(List.of(
                    FindProductListServiceResponseItem.builder()
                        .id(123L)
                        .productName("product1")
                        .sellerEmail("seller1")
                        .productImgUrl("img1")
                        .price(100000)
                        .build(),
                    FindProductListServiceResponseItem.builder()
                        .id(234L)
                        .productName("product2")
                        .sellerEmail("seller2")
                        .productImgUrl("img2")
                        .price(200000)
                        .build()))
                .build();
            when(findProductListUseCase.findProductList(any())).thenReturn(response);

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("query", request.getQuery())
                .param("category", request.getCategory())
                .param("sortType", request.getSortType()));

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.page").value(response.page()))
                .andExpect(jsonPath("$.data.size").value(response.size()))
                .andExpect(jsonPath("$.data.productCount").value(response.productCount()))
                .andExpect(jsonPath("$.data.productList").isArray());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 검색어를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .category("FASHION")
                .sortType("PRICE_DESC")
                .build();

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("query", request.getQuery())
                .param("category", request.getCategory())
                .param("sortType", request.getSortType()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("검색어는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 검색어를 빈 값으로 입력했을 떼 400코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .query("")
                .category("FASHION")
                .sortType("PRICE_DESC")
                .build();

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("query", request.getQuery())
                .param("category", request.getCategory())
                .param("sortType", request.getSortType()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("검색어는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 카테고리를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .query("test")
                .sortType("PRICE_DESC")
                .build();

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("query", request.getQuery())
                .param("category", request.getCategory())
                .param("sortType", request.getSortType()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("카테고리는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 카테고리를 빈 값으로 입력했을 떼 400코드와 에러 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .query("test")
                .category("")
                .sortType("PRICE_DESC")
                .build();

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("query", request.getQuery())
                .param("category", request.getCategory())
                .param("sortType", request.getSortType()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("카테고리는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 유효하지 않은 카테고리를 입력했을 떼 400코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .query("test")
                .category("error")
                .sortType("PRICE_DESC")
                .build();

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("query", request.getQuery())
                .param("category", request.getCategory())
                .param("sortType", request.getSortType()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("존재하지 않은 카테고리 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 정렬타입을 입력하지 않았을 떼 400코드와 에러 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .query("test")
                .category("FASHION")
                .build();

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("query", request.getQuery())
                .param("category", request.getCategory())
                .param("sortType", request.getSortType()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("정렬 타입은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 정렬타입을 빈 값으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error7() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .query("test")
                .category("FASHION")
                .sortType("")
                .build();

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("query", request.getQuery())
                .param("category", request.getCategory())
                .param("sortType", request.getSortType()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("정렬 타입은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 유효하지 않은 정렬타입을 입력했을 떄 400코드와 에러 메시지를 응답한다.")
        void error8() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .query("test")
                .category("FASHION")
                .sortType("error")
                .build();

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("query", request.getQuery())
                .param("category", request.getCategory())
                .param("sortType", request.getSortType()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("존재하지 않은 정렬 타입 입니다"))
                .andDo(print());
        }
    }

}
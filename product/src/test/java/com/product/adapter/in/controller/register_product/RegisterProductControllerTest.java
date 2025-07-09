package com.product.adapter.in.controller.register_product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.application.service.register_product.RegisterProductServiceResponse;
import com.product.domain.model.Category;
import com.product.infrastructure.exception.ErrorCode;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class RegisterProductControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[registerProduct] 상품을 등록하는 API")
    class Describe_registerProduct {


        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[success] 판매 권한을 가진 사용자가 필수값을 모두 입력하여 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            RegisterProductServiceResponse response = RegisterProductServiceResponse.builder()
                .productId(12345L)
                .sellerEmail("od@test.com")
                .productName(request.productName())
                .productImgUrl(request.productImgUrl())
                .descriptionImgUrl(request.descriptionImgUrl())
                .price(request.price())
                .quantity(request.quantity())
                .category(Category.valueOf(request.category()))
                .productOption(request.productOption())
                .build();
            String authorization = "testToken";
            given(registerProductUseCase.registerProduct(any())).willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.productId").value(response.productId()))
                .andExpect(jsonPath("$.data.sellerEmail").value(response.sellerEmail()))
                .andExpect(jsonPath("$.data.productName").value(response.productName()))
                .andExpect(jsonPath("$.data.productImgUrl").value(response.productImgUrl()))
                .andExpect(jsonPath("$.data.descriptionImgUrl").value(response.descriptionImgUrl()))
                .andExpect(jsonPath("$.data.price").value(response.price()))
                .andExpect(jsonPath("$.data.quantity").value(response.quantity()))
                .andExpect(jsonPath("$.data.category").value(response.category().name()))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder().build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
            RegisterProductRequest request = RegisterProductRequest.builder().build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName(null)
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품명은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 빈 값으로 입력했을 때400코드와 에러 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품명은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 50자 이상으로 입력했을 때400코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("test".repeat(13))
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
        @DisplayName("[error] 판매 권한을 가진 사용자가 카테고리를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category(null)
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("카테고리는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 카테고리를 빈 값으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error7() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("카테고리는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 유효하지 않은 카테고리를 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error8() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("unknown")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("존재하지 않은 카테고리 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 1미만의 금액을 입력했을 떄 400코드와 에러 메시지를 응답한다.")
        void error9() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(-1)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("금액은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 20 미만의 상품 수량을 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error10() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(10)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 수량은 20 이상이어야 합니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 이미지를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error11() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl(null)
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 이미지는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 이미지를 빈 값으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error12() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl("")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 이미지는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 50자 이상의 상품 이미지를 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error13() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl("test".repeat(13))
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 설명 이미지를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error14() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl(null)
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 설명 이미지는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 설명 이미지를 빈 값으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error15() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("")
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 설명 이미지는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 50자 이상의 상품 설명 이미지를 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error16() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("test".repeat(13))
                .productOption(Set.of("옵션1", "옵션2"))
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 옵션을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error17() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(null)
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 옵션은 필수값 입니다"))
                .andDo(print());
        }
    }

}
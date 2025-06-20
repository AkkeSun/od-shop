package com.product.adapter.in.controller.register_comment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.application.service.register_comment.RegisterCommentServiceResponse;
import com.product.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class RegisterCommentControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[registerComment] 리뷰를 등록하는 API")
    class Describe_registerComment {

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 등록 권한을 가진 사용자가 필수값을 모두 입력하여 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("테스트리뷰테스트리뷰")
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            when(registerCommentUseCase.registerComment(any()))
                .thenReturn(RegisterCommentServiceResponse.ofSuccess());

            // when
            ResultActions actions = mockMvc.perform(
                post("/products/{productId}/comments", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", authorization)
                    .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.result").value(true));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder().build();
            String authorization = "testToken";
            Long productId = 1L;

            // when
            ResultActions actions = mockMvc.perform(
                post("/products/{productId}/comments", productId)
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
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 등록 권한이 없는 사용자가 API 를 요청 했을 때 403코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder().build();
            String authorization = "testToken";
            Long productId = 1L;

            // when
            ResultActions actions = mockMvc.perform(
                post("/products/{productId}/comments", productId)
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
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 등록 권한을 가진 사용자가 리뷰를 빈 값으로 입력했을 때 400코드와 성공 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment(null)
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            when(registerCommentUseCase.registerComment(any()))
                .thenReturn(RegisterCommentServiceResponse.ofSuccess());

            // when
            ResultActions actions = mockMvc.perform(
                post("/products/{productId}/comments", productId)
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
                .andExpect(jsonPath("$.data.errorMessage").value("코멘트는 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 등록 권한을 가진 사용자가 리뷰를 빈 문자열로 입력했을 때 400코드와 성공 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("")
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            when(registerCommentUseCase.registerComment(any()))
                .thenReturn(RegisterCommentServiceResponse.ofSuccess());

            // when
            ResultActions actions = mockMvc.perform(
                post("/products/{productId}/comments", productId)
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
                .andExpect(jsonPath("$.data.errorMessage").value("코멘트는 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 등록 권한을 가진 사용자가 10자 미만의 리뷰를 입력했을 때 400코드와 성공 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("test")
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            when(registerCommentUseCase.registerComment(any()))
                .thenReturn(RegisterCommentServiceResponse.ofSuccess());

            // when
            ResultActions actions = mockMvc.perform(
                post("/products/{productId}/comments", productId)
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
                .andExpect(jsonPath("$.data.errorMessage").value("코멘트는 10자 이상 50자 이하여야 합니다."))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 등록 권한을 가진 사용자가 50 초과의 리뷰를 입력했을 때 400코드와 성공 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("test".repeat(20))
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            when(registerCommentUseCase.registerComment(any()))
                .thenReturn(RegisterCommentServiceResponse.ofSuccess());

            // when
            ResultActions actions = mockMvc.perform(
                post("/products/{productId}/comments", productId)
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
                .andExpect(jsonPath("$.data.errorMessage").value("코멘트는 10자 이상 50자 이하여야 합니다."))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 등록 권한을 가진 사용자가 점수를 입력하지 않았을 때 400코드와 성공 메시지를 응답한다.")
        void error7() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("테스트리스테스트리뷰")
                .score(null)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            when(registerCommentUseCase.registerComment(any()))
                .thenReturn(RegisterCommentServiceResponse.ofSuccess());

            // when
            ResultActions actions = mockMvc.perform(
                post("/products/{productId}/comments", productId)
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
                .andExpect(jsonPath("$.data.errorMessage").value("점수는 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 등록 권한을 가진 사용자가 0.5점 미만의 점수를 입력했을 때 400코드와 성공 메시지를 응답한다.")
        void error8() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("테스트리스테스트리뷰")
                .score(0.4)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            when(registerCommentUseCase.registerComment(any()))
                .thenReturn(RegisterCommentServiceResponse.ofSuccess());

            // when
            ResultActions actions = mockMvc.perform(
                post("/products/{productId}/comments", productId)
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
                .andExpect(jsonPath("$.data.errorMessage").value("점수는 0.5 이상 5.0 이하여야 합니다."))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 등록 권한을 가진 사용자가 5점 초과의 점수를 입력했을 때 400코드와 성공 메시지를 응답한다.")
        void error9() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("테스트리스테스트리뷰")
                .score(5.1)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            when(registerCommentUseCase.registerComment(any()))
                .thenReturn(RegisterCommentServiceResponse.ofSuccess());

            // when
            ResultActions actions = mockMvc.perform(
                post("/products/{productId}/comments", productId)
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
                .andExpect(jsonPath("$.data.errorMessage").value("점수는 0.5 이상 5.0 이하여야 합니다."))
                .andDo(print());
        }
    }
}
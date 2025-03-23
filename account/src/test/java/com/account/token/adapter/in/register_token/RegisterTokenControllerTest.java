package com.account.token.adapter.in.register_token;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.account.ControllerTestSupport;
import com.account.token.application.service.register_token.RegisterTokenServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultActions;

class RegisterTokenControllerTest extends ControllerTestSupport {

    @Nested
    @WithAnonymousUser
    @DisplayName("[registerToken] 사용자 토큰을 등록하는 API")
    class Describe_RegisterToken {

        @Test
        @DisplayName("[success] 필수값을 올바르게 입력했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            RegisterTokenRequest request = RegisterTokenRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();
            RegisterTokenServiceResponse response = RegisterTokenServiceResponse.builder()
                .accessToken("Bearer testAccessToken")
                .refreshToken("Bearer testRefreshToken")
                .build();
            given(registerTokenUseCase.registerToken(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.accessToken").value(response.accessToken()))
                .andExpect(jsonPath("$.data.refreshToken").value(response.refreshToken()))
                .andDo(print());
        }


        @Test
        @DisplayName("[error] 이메일을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            RegisterTokenRequest request = RegisterTokenRequest.builder()
                .password("1234")
                .build();

            // when
            ResultActions actions = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("이메일은 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @DisplayName("[error] 비밀번호를 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            RegisterTokenRequest request = RegisterTokenRequest.builder()
                .email("test@gmail.com")
                .build();

            // when
            ResultActions actions = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("비밀번호는 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @DisplayName("[error] 이메일을 빈 값으로 입력했을 때 400 코드와 오류 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            RegisterTokenRequest request = RegisterTokenRequest.builder()
                .email("")
                .password("1234")
                .build();

            // when
            ResultActions actions = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("이메일은 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @DisplayName("[error] 비밀번호를 빈 값으로 입력했을 때 400 코드와 오류 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            RegisterTokenRequest request = RegisterTokenRequest.builder()
                .email("test@gmail.com")
                .password("")
                .build();

            // when
            ResultActions actions = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("비밀번호는 필수값 입니다."))
                .andDo(print());
        }
    }

}
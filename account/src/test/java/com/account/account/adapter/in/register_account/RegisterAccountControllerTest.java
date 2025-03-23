package com.account.account.adapter.in.register_account;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.account.ControllerTestSupport;
import com.account.account.applicaiton.service.register_account.RegisterAccountServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultActions;

class RegisterAccountControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[registerAccount] 사용자 정보를 등록하는 API")
    class Describe_registerAccount {

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 필수값을 모두 입력했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("od@test.com")
                .password("1234")
                .passwordCheck("1234")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
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
        @WithAnonymousUser
        @DisplayName("[error] 이메일을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .password("1234")
                .passwordCheck("1234")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
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
        @WithAnonymousUser
        @DisplayName("[error] 이메일을 빈 값으로 입력했을 때 400 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("")
                .password("1234")
                .passwordCheck("1234")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
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
        @WithAnonymousUser
        @DisplayName("[error] 비밀번호를 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .passwordCheck("1234")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
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
        @WithAnonymousUser
        @DisplayName("[error] 비밀번호를 빈 값으로 입력했을 때 400 코드와 오류 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("")
                .passwordCheck("1234")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
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
        @WithAnonymousUser
        @DisplayName("[error] 비밀번호 확인을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("비밀번호 확인은 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 비밀번호 확인을 빈 값으로 입력했을 때 400 코드와 오류 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("비밀번호 확인은 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 권한을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error7() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("권한은 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 권한을 빈 값으로 입력했을 때 400 코드와 오류 메시지를 응답한다.")
        void error8() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("1234")
                .role("")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("권한은 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 비밀번호와 비밀번호 확인이 동일하지 않을 때 400 코드와 오류 메시지를 응답한다.")
        void error9() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("12345")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("비밀번호와 비밀번호 확인이 일치하지 않습니다."))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 유효한 권한을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error10() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("1234")
                .role("error")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("유효하지 않은 권한 입니다."))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 올바른 전화번호 형식을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error11() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("1234")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("111")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("올바른 전화번호 형식이 아닙니다."))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 이메일을 50자 이상으로 입력했을 때 400 코드와 에러 메시지를 응답한다.")
        void error12() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("12345".repeat(11))
                .password("1234")
                .passwordCheck("1234")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("이메일은 50자 이하로 입력 가능 합니다."))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 이름을 10자 이상으로 입력했을 때 400 코드와 에러 메시지를 응답한다.")
        void error13() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("12345")
                .password("1234")
                .passwordCheck("1234")
                .role("ROLE_CUSTOMER")
                .username("od".repeat(6))
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("이름은 10자 이하로 입력 가능 합니다."))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 주소를 100자 이상으로 입력했을 때 400 코드와 에러 메시지를 응답한다.")
        void error14() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("12345")
                .password("1234")
                .passwordCheck("1234")
                .role("ROLE_CUSTOMER")
                .username("od".repeat(6))
                .userTel("01012345678")
                .address("12345".repeat(21))
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("주소는 100자 이하로 입력 가능 합니다."))
                .andDo(print());
        }
    }
}
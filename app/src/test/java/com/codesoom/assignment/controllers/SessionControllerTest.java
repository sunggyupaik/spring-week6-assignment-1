package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.AuthenticationCreateData;
import com.codesoom.assignment.dto.SessionResultData;
import com.codesoom.assignment.errors.UserBadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(SessionController.class)
@DisplayName("SessionController 테스트")
class SessionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationService authenticationService;

    private static final String SECRET = "12345678901234567890123456789010";
    private static final String EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4aXN0ZWRFbWFpbCIsInBhc3N3b3JkIjoiZXhpc3RlZFBhc3N3b3JkIn0." +
            "iqS2XKpt7blLuhlACfLFdomPsjXzC9RGW67mJGB0NaA";
    private static final String NOT_EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4aXN0ZWRFbWFpbCIsInBhc3N3b3JkIjoiZXhpc3RlZFBhc3N3b3JkIn0." +
            "iqS2XKpt7blLuhlACfLFdomPsjXzC9RGW67mJGB0Naa";

    private final String EXISTED_EMAIL = "existedEmail";
    private final String EXISTED_PASSWORD = "existedPassword";

    private final String NOT_EXISTED_EMAIL = "notExistedEmail";
    private final String NOT_EXISTED_PASSWORD = "notExistedPassword";

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("저장되어 있는 사용자가 주어진다면")
        class Context_WithExistedUser {
            private SessionResultData sessionResultData;

            @BeforeEach
            void setUp() {
                sessionResultData = SessionResultData.builder()
                        .accessToken(EXISTED_TOKEN)
                        .build();
            }

            @Test
            @DisplayName("주어진 사용자로 토큰을 생성하고 해당 토큰과 CREATED를 리턴한다")
            void itCreatesTokenAndReturnsTokenAndCREATEDHttpStatus() throws Exception {
                given(authenticationService.createToken(any(AuthenticationCreateData.class))).willReturn(sessionResultData);

                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"existedEmail\",\"password\":\"existedPassword\"}"))
                        .andDo(print())
                        .andExpect(status().isCreated());
            }
        }

        @Nested
        @DisplayName("저장되어 있지 않은 사용자가 주어진다면")
        class Context_WithNotExistedUser {
            @Test
            @DisplayName("요청이 잘못 되었다는 메세지와 BAD_REQUEST를 리턴한다")
            void itReturnsBadRequestMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(authenticationService.createToken(any(AuthenticationCreateData.class)))
                        .willThrow(new UserBadRequestException());

                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"notExistedEmail\",\"password\":\"notExistedPassword\"}"))
                        .andDo(print())
                        .andExpect(content().string(containsString("User bad request")))
                        .andExpect(status().isBadRequest());
            }

        }
    }
}

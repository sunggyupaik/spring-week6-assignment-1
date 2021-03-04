//package com.codesoom.assignment.controllers;
//
//import com.codesoom.assignment.application.AuthenticationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@WebMvcTest(SessionController.class)
//class SessionControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    AuthenticationService authenticationService;
//
//    private final Long EXISTED_ID = 1L;
//
//    @BeforeEach
//    void setUp() {
//        given(authenticationService.login(EXISTED_ID)).willReturn("a.b.c");
//    }
//
//    @Test
//    void login() throws Exception {
//        mockMvc.perform(post("/session"))
//                .andExpect(status().isCreated())
//                .andExpect(content().string(containsString(".")));
//    }
//}

package org.example.delivery.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.delivery.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc // 얘는 다 한다
class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AuthService authService;

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void 회원가입() {
    //given

    //when

    //then
  }

  @Test
  void 로그인() {
  }

  @Test
  void 회원탈퇴() {
  }
}
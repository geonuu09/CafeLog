package com.project.cafelogproject.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.AddUserRequestDTO;
import com.project.cafelogproject.dto.LoginRequestDTO;
import com.project.cafelogproject.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll(); // 테스트 전 데이터베이스 초기화
  }

  @Test
  void register_success() throws Exception {
    // given
    AddUserRequestDTO request = new AddUserRequestDTO();
    request.setEmail("test@example.com");
    request.setPassword("password123");
    request.setNickname("testuser");

    // when
    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().string("User registered with id 1"));

    // then
    User savedUser = userRepository.findByEmail("test@example.com").orElseThrow(() -> new IllegalArgumentException("User not found"));
    Assertions.assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    Assertions.assertThat(savedUser.getNickname()).isEqualTo("testuser");
  }


  @Test
  void login_success() throws Exception {
    // given
    AddUserRequestDTO registerRequest = new AddUserRequestDTO();
    registerRequest.setEmail("test@example.com");
    registerRequest.setPassword("password123");
    registerRequest.setNickname("testuser");

    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().string("User registered with id 1"));


    LoginRequestDTO loginRequest = new LoginRequestDTO();
    loginRequest.setEmail("test@example.com");
    loginRequest.setPassword("password123");

    // when
    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(result -> {
          String response = result.getResponse().getContentAsString();
          Assertions.assertThat(response).startsWith("Bearer ");
          Assertions.assertThat(response).contains(".");
        });
  }

}

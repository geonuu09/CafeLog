package com.project.cafelogproject.controller;

import com.project.cafelogproject.dto.AddUserRequestDTO;
import com.project.cafelogproject.dto.LoginRequestDTO;
import com.project.cafelogproject.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid AddUserRequestDTO request) {

    if (authService.existsByEmail(request.getEmail())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
    }
    log.info("Registering user {}", request.getEmail());
    Long userId = authService.save(request); // 회원가입 매서드 호출
    log.info("User {} registered", userId);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("User registered with id " + userId); // 회원가입 완료 메시지 반환
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
    log.info("Login request {}", request);

    try {
      String token = authService.login(request);
      return ResponseEntity.ok().body("Bearer " + token);
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }

}

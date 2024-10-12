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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid AddUserRequestDTO request) {
    Long userId = authService.save(request);
    log.info("User {} registered", userId);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("User registered with id " + userId);
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
    log.info("Login request {}", request);

    try {
      String token = authService.login(request);
      return ResponseEntity.ok().body("Bearer " + token);
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }

}

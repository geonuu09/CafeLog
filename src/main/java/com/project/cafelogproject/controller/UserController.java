package com.project.cafelogproject.controller;

import com.project.cafelogproject.dto.AddUserRequestDTO;
import com.project.cafelogproject.dto.LoginRequestDTO;
import com.project.cafelogproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid AddUserRequestDTO request) {
    log.info("Registering user {}", request.getEmail());
    Long userId = userService.save(request);
    log.info("User {} registered", userId);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("User registered with id " + userId);
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO request) {
    log.info("Login request for user: {}", request.getEmail());
    String token = userService.login(request);
    return ResponseEntity.ok("Bearer " + token);
  }
}
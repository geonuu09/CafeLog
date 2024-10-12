package com.project.cafelogproject.controller;

import com.project.cafelogproject.config.exception.CustomException;
import com.project.cafelogproject.dto.AddUserRequestDTO;
import com.project.cafelogproject.dto.LoginRequestDTO;
import com.project.cafelogproject.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
    log.info("이메일에 대한 로그인 요청: {}", request.getEmail());
    try {
      String token = authService.login(request);
      return ResponseEntity.ok().body("Bearer " + token);
    } catch (CustomException e) {
      log.error("로그인 실패: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(e.getMessage());
    } catch (Exception e) {
      log.error("로그인 중 예상치 못한 오류 발생: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("예상치 못한 오류가 발생했습니다");
    }
  }
}

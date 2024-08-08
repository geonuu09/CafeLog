package com.project.cafelogproject.controller;

import com.project.cafelogproject.dto.AddUserRequest;
import com.project.cafelogproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid AddUserRequest request) {

    if(userService.existsByEmail(request.getEmail())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
    }
    Long userId = userService.save(request); // 회원가입 매서드 호출
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("User registered with id " + userId); // 회원가입 완료 메시지 반환
  }

}

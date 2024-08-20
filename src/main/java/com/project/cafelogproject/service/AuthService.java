package com.project.cafelogproject.service;

import com.project.cafelogproject.config.jwt.TokenProvider;
import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.AddUserRequestDTO;
import com.project.cafelogproject.dto.LoginRequestDTO;
import com.project.cafelogproject.repository.UserRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;

  public Long save(AddUserRequestDTO dto) {
    return userRepository.save(User.builder()
        .email(dto.getEmail())
        .password(passwordEncoder.encode(dto.getPassword()))
        .nickname(dto.getNickname())
        .createdDate(LocalDateTime.now())
        .build()).getId();
  }

  public boolean existsByEmail(String email) {
    return userRepository.findByEmail(email).isPresent();
  }

  public String login(LoginRequestDTO dto) {
    try {
      // 사용자 인증
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
      log.info("Authentication successful for email : {}", dto.getEmail());

      // 인증 성공 시 JWT 생성
      User user = (User) authentication.getPrincipal();
      return tokenProvider.generateToken(user, Duration.ofHours(1));
    } catch (AuthenticationException e) {
      log.error("Authentication failed for email : {}", dto.getEmail(), e);
      throw new RuntimeException("Invalid email or password", e);
    }
  }
}
package com.project.cafelogproject.service;

import com.project.cafelogproject.config.exception.CustomException;
import com.project.cafelogproject.config.exception.ErrorCode;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;
  private final AuthenticationManager authenticationManager;

  public Long save(AddUserRequestDTO dto) {
    if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
      throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
    }

    return userRepository.save(User.builder()
        .email(dto.getEmail())
        .password(passwordEncoder.encode(dto.getPassword()))
        .nickname(dto.getNickname())
        .createdDate(LocalDateTime.now())
        .build()).getId();
  }

  public String login(LoginRequestDTO dto) {
    User user = userRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
      );
      log.info("이메일 인증 성공: {}", dto.getEmail());

      return tokenProvider.generateToken(user, Duration.ofHours(1));
    } catch (BadCredentialsException e) {
      log.error("잘못된 자격 증명: {}", dto.getEmail(), e);
      throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
    } catch (UsernameNotFoundException e) {
      log.error("사용자를 찾을 수 없음: {}", dto.getEmail(), e);
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    } catch (CustomException e) {
      log.error("로그인 중 커스텀 예외 발생: {}", e.getMessage(), e);
      throw e;
    } catch (Exception e) {
      log.error("로그인 중 예기치 않은 오류 발생", e);
      throw new CustomException(ErrorCode.LOGIN_FAILED);
    }
  }

}
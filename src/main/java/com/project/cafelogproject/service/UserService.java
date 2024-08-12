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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final TokenProvider tokenProvider;

  @Transactional
  public Long save(AddUserRequestDTO dto) {
    if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
      throw new CustomException(ErrorCode.DUPLICATE_EMAIL); // 이미 존재하는 이메일
    }
    User user = User.builder()
        .email(dto.getEmail())
        .password(passwordEncoder.encode(dto.getPassword()))
        .nickname(dto.getNickname())
        .createdDate(LocalDateTime.now())
        .build();
    return userRepository.save(user).getId();
  }

  @Transactional
  public String login(LoginRequestDTO dto) {
    try {
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
      log.info("Authentication successful for email : {}", dto.getEmail());

      User user = (User) authentication.getPrincipal();
      return tokenProvider.generateToken(user, Duration.ofHours(1));
    } catch (Exception e) {
      throw new CustomException(ErrorCode.LOGIN_FAILED);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }
}
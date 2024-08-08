package com.project.cafelogproject.service;

import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.AddUserRequest;
import com.project.cafelogproject.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  public Long save(AddUserRequest dto) {

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

//  public User findById(Long userId) {
//    return userRepository.findById(userId)
//        .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
//  }
//
//  public User findByEmail(String email) {
//    return userRepository.findByEmail(email)
//        .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
//  }
}

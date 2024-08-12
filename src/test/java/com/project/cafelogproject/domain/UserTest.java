package com.project.cafelogproject.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;

class UserTest {

  /**
   * User 객체 생성 테스트
   * 빌더 패턴을 사용하여 User 객체를 생성하고, 각 필드가 올바르게 설정되었는지 검증합니다.
   */
  @Test
  void testUserCreation() {
    // Given: User 객체 생성에 필요한 데이터 준비
    LocalDateTime now = LocalDateTime.now();

    // When: User 객체 생성
    User user = User.builder()
        .email("test@example.com")
        .password("password123")
        .nickname("testUser")
        .createdDate(now)
        .build();

    // Then: 생성된 User 객체의 각 필드 검증
    assertThat(user).isNotNull();
    assertThat(user.getEmail()).isEqualTo("test@example.com");
    assertThat(user.getPassword()).isEqualTo("password123");
    assertThat(user.getNickname()).isEqualTo("testUser");
    assertThat(user.getCreatedDate()).isEqualTo(now);
    assertThat(user.getModifiedDate()).isNull();
  }

  /**
   * 닉네임 업데이트 테스트
   * User 객체의 닉네임을 변경하고, 변경이 올바르게 적용되었는지 검증합니다.
   */
  @Test
  void testUpdateNickname() {
    // Given: 초기 닉네임을 가진 User 객체 생성
    User user = User.builder()
        .email("test@example.com")
        .password("password123")
        .nickname("oldNickname")
        .createdDate(LocalDateTime.now())
        .build();

    // When: 닉네임 업데이트 메서드 호출
    user.updateNickname("newNickname");

    // Then: 업데이트된 닉네임 검증
    assertThat(user.getNickname()).isEqualTo("newNickname");
  }

  /**
   * 비밀번호 업데이트 테스트
   * User 객체의 비밀번호를 변경하고, 변경이 올바르게 적용되었는지 검증합니다.
   */
  @Test
  void testUpdatePassword() {
    // Given: 초기 비밀번호를 가진 User 객체 생성
    User user = User.builder()
        .email("test@example.com")
        .password("oldPassword")
        .nickname("testUser")
        .createdDate(LocalDateTime.now())
        .build();

    // When: 비밀번호 업데이트 메서드 호출
    user.updatePassword("newPassword");

    // Then: 업데이트된 비밀번호 검증
    assertThat(user.getPassword()).isEqualTo("newPassword");
  }


}
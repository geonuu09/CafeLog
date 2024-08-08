package com.project.cafelogproject.repository;

import com.project.cafelogproject.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email); // email로 사용자 정보를 가져옴

}

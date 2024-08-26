package com.project.cafelogproject.repository;

import com.project.cafelogproject.domain.Favorite;
import com.project.cafelogproject.domain.Post;
import com.project.cafelogproject.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
  Page<Favorite> findAllByUser(User user, Pageable pageable);
  boolean existsByUserAndPost(User user, Post post);
  void deleteByUserAndPost(User user, Post post);

}

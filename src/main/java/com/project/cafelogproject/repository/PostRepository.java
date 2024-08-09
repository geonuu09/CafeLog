package com.project.cafelogproject.repository;

import com.project.cafelogproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}

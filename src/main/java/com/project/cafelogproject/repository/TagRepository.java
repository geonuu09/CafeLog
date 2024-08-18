package com.project.cafelogproject.repository;

import com.project.cafelogproject.domain.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
  Optional<Tag> findByName(String name);

}

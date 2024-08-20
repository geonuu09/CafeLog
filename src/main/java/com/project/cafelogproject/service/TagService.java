package com.project.cafelogproject.service;

import com.project.cafelogproject.domain.Tag;
import com.project.cafelogproject.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {
  private final TagRepository tagRepository;

  @Transactional
  public Tag getOrCreateTag(String tagName) {
    String normalizedName = normalizeTagName(tagName);
    return tagRepository.findByNormalizedName(normalizedName)
        .orElseGet(() -> createTag(tagName, normalizedName));
  }

  private Tag createTag(String originalName, String normalizedName) {
    Tag tag = new Tag(originalName, normalizedName);
    return tagRepository.save(tag);
  }

  private String normalizeTagName(String name) {
    return name.toLowerCase().replaceAll("\\s+", "");
  }
}

package com.project.cafelogproject.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailResponseDTO {
  private Long id;
  private String cafeName;
  private String content;
  private String authorName;
  private LocalDateTime createdDate;
  private List<String> tags;
}
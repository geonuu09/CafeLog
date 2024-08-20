package com.project.cafelogproject.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailDTO {
  private String cafeName;
  private String address;
  private Boolean recommend;
  private String content;
  private String userEmail;
  private LocalDateTime createdDate;
  private List<String> tags;
}
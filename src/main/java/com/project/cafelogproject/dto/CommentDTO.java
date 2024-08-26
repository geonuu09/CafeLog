package com.project.cafelogproject.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class CommentDTO {
  private Long id;

  @NotBlank
  private String content;

  private String userEmail;
  private LocalDateTime createdDate;


}

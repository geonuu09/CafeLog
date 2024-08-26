package com.project.cafelogproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentDTO {
  @NotBlank
  private String content;
}

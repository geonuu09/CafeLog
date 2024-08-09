package com.project.cafelogproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatePostRequestDTO {

  @NotBlank
  private String name;

  @NotBlank
  private String address;

  private Boolean recommend;

  @NotBlank
  private String description;

  private Boolean isPublic;
}
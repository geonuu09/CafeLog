package com.project.cafelogproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddPostRequestDTO {

  @NotBlank
  private String cafeName;

  @NotBlank
  private String address;

  private Boolean recommend;

  @NotBlank
  private String content;

  private Boolean isPublic;
}
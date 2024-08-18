package com.project.cafelogproject.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
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
  private List<String> tags;
  @NotBlank
  private String content;

  private Boolean isPublic;
}
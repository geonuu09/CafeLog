package com.project.cafelogproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdatePostRequestDTO {
  @NotBlank
  private String cafeName;
  @NotBlank
  private String address;
  private Boolean recommend;
  @NotBlank
  private String content;
  private Boolean isPublic;
  @Valid
  private List<@NotBlank @Size(max = 20) String> tags;
}
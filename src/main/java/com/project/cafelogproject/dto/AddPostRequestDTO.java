package com.project.cafelogproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

  @Valid
  private List<@NotBlank @Size(max = 20) String> tags;
  @NotBlank
  private String content;

  private Boolean isPublic;
}
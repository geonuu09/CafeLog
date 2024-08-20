package com.project.cafelogproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdatePostRequestDTO {
  private String cafeName;
  private String address;
  private Boolean recommend;
  private String content;
  private Boolean isPublic;
  private List<String> tags;
}
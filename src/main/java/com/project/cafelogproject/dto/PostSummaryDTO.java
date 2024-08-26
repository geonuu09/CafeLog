package com.project.cafelogproject.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryDTO {
  private Long id;
  private String cafeName;
  private List<String> tags;
}

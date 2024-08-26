package com.project.cafelogproject.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteWithPostSummaryDTO {
  private Long favoriteId;
  private LocalDateTime createdDate;
  private PostSummaryDTO post;

}


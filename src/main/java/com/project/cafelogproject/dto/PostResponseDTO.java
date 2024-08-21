package com.project.cafelogproject.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDTO {
  private Long id;
  private String cafeName;        // 카페 이름
  private String address;     // 카페 주소
  private Boolean recommend;  // 추천 여부
  private String content; // 설명
  private Boolean isPublic;   // 공개 여부
  private String userEmail;   // 작성자 이메일
  private LocalDateTime createdDate;
  private List<String> tags;
}

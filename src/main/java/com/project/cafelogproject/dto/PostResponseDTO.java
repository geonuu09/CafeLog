package com.project.cafelogproject.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDTO {
  private Long id;
  private String name;        // 카페 이름
  private String address;     // 카페 주소
  private Boolean recommend;  // 추천 여부
  private String description; // 설명
  private Boolean isPublic;   // 공개 여부
  private String userEmail;   // 작성자 이메일
}

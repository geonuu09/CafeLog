package com.project.cafelogproject.controller;

import com.project.cafelogproject.dto.AddPostRequestDTO;
import com.project.cafelogproject.dto.PostDetailDTO;
import com.project.cafelogproject.dto.PostResponseDTO;
import com.project.cafelogproject.dto.UpdatePostRequestDTO;
import com.project.cafelogproject.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

  // PostService를 주입받아 사용합니다.
  private final PostService postService;

  /**
   * 새 게시글 작성 API
   *
   * @param request     게시글 생성 요청 데이터를 담고 있는 DTO
   * @param userDetails 인증된 사용자 정보
   * @return 생성된 게시글의 상세 정보와 함께 201(Created) 상태 코드 반환
   */
  @PostMapping("/write")
  public ResponseEntity<PostDetailDTO> addPost(@Valid @RequestBody AddPostRequestDTO request,
      @AuthenticationPrincipal UserDetails userDetails) {

    PostDetailDTO response = postService.addPost(request, userDetails.getUsername());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 게시글 수정 API
   *
   * @param id          수정할 게시글의 ID
   * @param updateDTO   게시글 수정 요청 데이터를 담고 있는 DTO
   * @param userDetails 인증된 사용자 정보
   * @return 수정된 게시글의 요약 정보와 함께 200(OK) 상태 코드 반환
   */
  @PutMapping("/{id}")
  public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id,
      @RequestBody @Valid UpdatePostRequestDTO updateDTO,
      @AuthenticationPrincipal UserDetails userDetails) {
    PostResponseDTO updatedPost = postService.updatePost(id, updateDTO, userDetails.getUsername());
    return ResponseEntity.ok(updatedPost);
  }

  /**
   * 게시글 검색 API
   *
   * @param query    검색할 키워드 (예: 태그 이름, 카페 이름)
   * @param pageable 페이지네이션과 정렬 정보
   * @return 검색된 게시글 목록과 함께 200(OK) 상태 코드 반환
   */
  @GetMapping("/search")
  public ResponseEntity<Page<PostDetailDTO>> searchPosts(@RequestParam String query,
      @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.searchPosts(query, pageable));
  }

  /**
   * 모든 게시글 조회 API
   *
   * @param pageable 페이지네이션과 정렬 정보
   * @return 모든 게시글 목록과 함께 200(OK) 상태 코드 반환
   */
  @GetMapping
  public ResponseEntity<Page<PostDetailDTO>> getAllPosts(
      @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.getAllPosts(pageable));
  }

  /**
   * 게시글 삭제 API
   *
   * @param id          삭제할 게시글의 ID
   * @param userDetails 인증된 사용자 정보
   * @return 성공 시 204(No Content) 상태 코드 반환, 인증되지 않은 경우 401(Unauthorized) 상태 코드 반환
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletePost(@PathVariable Long id,
      @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }
    postService.deletePost(id, userDetails.getUsername());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

  }
}

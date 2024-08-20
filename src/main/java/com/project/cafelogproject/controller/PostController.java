package com.project.cafelogproject.controller;

import com.project.cafelogproject.config.exception.CustomException;
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

  private final PostService postService;

  /**
   * 새 게시글 작성 API
   *
   * @param request     AddPostRequestDTO - 게시글의 제목, 내용 등 포함.
   * @param userDetails 인증된 사용자의 정보
   */
  @PostMapping("/write")
  public ResponseEntity<PostDetailDTO> addPost(@RequestBody AddPostRequestDTO request,
      @AuthenticationPrincipal UserDetails userDetails) {
    log.info("Received request to add post from user: {}", userDetails.getUsername());
    try {

      PostDetailDTO response = postService.addPost(request, userDetails.getUsername());
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (CustomException e) {
      log.error("Error occurred while adding post: {}", e.getMessage());
      throw e;
    }
  }

  /**
   * 게시글 수정 API
   *
   * @param id          수정할 게시글 ID.
   * @param updateDTO   UpdatePostRequestDTO - 수정할 제목, 내용 등 포함.
   * @param userDetails 인증된 사용자의 정보.
   */
  @PutMapping("/{id}")
  public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id,
      @RequestBody @Valid UpdatePostRequestDTO updateDTO,
      @AuthenticationPrincipal UserDetails userDetails) {
    PostResponseDTO updatedPost = postService.updatePost(id, updateDTO, userDetails.getUsername());
    return ResponseEntity.ok(updatedPost);
  }

  /**
   * 게시글 검색 API 특정 키워드(query)를 사용해 게시글을 검색할 때 호출.
   *
   * @param query    검색할 키워드 (Tag name, Cafe name)
   * @param pageable 페이지네이션과 정렬 정보.
   */
  @GetMapping("/search")
  public ResponseEntity<Page<PostDetailDTO>> searchPosts(@RequestParam String query,
      @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.searchPosts(query, pageable));
  }

  /**
   * 모든 게시글 조회 API (페이징 적용) 모든 게시글을 조회할 때 호출.
   *
   * @param pageable
   */
  @GetMapping
  public ResponseEntity<Page<PostDetailDTO>> getAllPosts(
      @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
    // PostService를 통해 모든 게시글 리스트를 반환합니다.
    return ResponseEntity.ok(postService.getAllPosts(pageable));
  }

  /**
   * 게시글 삭제 API 특정 게시글을 삭제할 때 호출.
   *
   * @param id          게시글 ID.
   * @param userDetails 인증된 사용자의 정보.
   * @return 성공시 204(NO CONTENT)를 반환.
   * 인증되지 않은 사용자인 경우 401(UNAUTHORIZED)를 반환.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletePost(@PathVariable Long id,
      @AuthenticationPrincipal UserDetails userDetails) {
    // 사용자가 인증되지 않은 경우
    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }
    log.info("Received request to delete post with id: {} from user: {}", id,
        userDetails.getUsername());
    try {
      postService.deletePost(id, userDetails.getUsername());
      return ResponseEntity.noContent().build();
    } catch (CustomException e) {
      log.error("Error occurred while deleting post: {}", e.getMessage());
      throw e;
    }
  }
}

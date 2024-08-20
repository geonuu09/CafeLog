package com.project.cafelogproject.controller;

import com.project.cafelogproject.config.exception.CustomException;
import com.project.cafelogproject.dto.AddPostRequestDTO;
import com.project.cafelogproject.dto.PostDetailDTO;
import com.project.cafelogproject.service.PostService;
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

  @GetMapping("/search")
  public ResponseEntity<Page<PostDetailDTO>> searchPosts(@RequestParam String query,
      @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.searchPosts(query, pageable));
  }

  @GetMapping
  public ResponseEntity<Page<PostDetailDTO>> getAllPosts(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.getAllPosts(pageable));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }
    log.info("Received request to delete post with id: {} from user: {}", id, userDetails.getUsername());
    try {
      postService.deletePost(id, userDetails.getUsername());
      return ResponseEntity.noContent().build();
    } catch (CustomException e) {
      log.error("Error occurred while deleting post: {}", e.getMessage());
      throw e;
    }
  }
}
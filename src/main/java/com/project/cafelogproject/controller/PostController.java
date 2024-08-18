package com.project.cafelogproject.controller;

import com.project.cafelogproject.config.exception.CustomException;
import com.project.cafelogproject.config.exception.ErrorResponse;
import com.project.cafelogproject.dto.AddPostRequestDTO;
import com.project.cafelogproject.dto.PostDetailResponseDTO;
import com.project.cafelogproject.dto.PostResponseDTO;
import com.project.cafelogproject.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
  public ResponseEntity<PostResponseDTO> addPost(@RequestBody AddPostRequestDTO request,
      @AuthenticationPrincipal UserDetails userDetails) {
    log.info("Received request to add post from user: {}", userDetails.getUsername());
    try {
      PostResponseDTO response = postService.addPost(request, userDetails.getUsername());
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (CustomException e) {
      log.error("Error occurred while adding post: {}", e.getMessage());
      throw e;
    }
  }


  @GetMapping("/search")
  public ResponseEntity<?> searchPosts(@RequestParam String query) {
    try {
      List<PostResponseDTO> posts = postService.searchPosts(query);
      return ResponseEntity.ok(posts);
    } catch (CustomException e) {
      return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
  }



}

package com.project.cafelogproject.service;

import com.project.cafelogproject.domain.Post;
import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.CreatePostRequestDTO;
import com.project.cafelogproject.dto.PostResponseDTO;
import com.project.cafelogproject.repository.PostRepository;
import com.project.cafelogproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Transactional
  public PostResponseDTO createPost(CreatePostRequestDTO request, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Post post = Post.builder()
        .name(request.getName())
        .address(request.getAddress())
        .recommend(request.getRecommend())
        .description(request.getDescription())
        .isPublic(request.getIsPublic())
        .user(user)
        .build();

    Post savedPost = postRepository.save(post);

    return mapToPostResponse(savedPost);
  }

  private PostResponseDTO mapToPostResponse(Post post) {
    return PostResponseDTO.builder()
        .id(post.getId())
        .name(post.getName())
        .address(post.getAddress())
        .recommend(post.getRecommend())
        .description(post.getDescription())
        .isPublic(post.getIsPublic())
        .userEmail(post.getUser().getEmail())  // 작성자 이메일 추가
        .build();
  }
}
package com.project.cafelogproject.service;

import com.project.cafelogproject.config.exception.CustomException;
import com.project.cafelogproject.config.exception.ErrorCode;
import com.project.cafelogproject.domain.Post;
import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.AddPostRequestDTO;
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
  public PostResponseDTO addPost(AddPostRequestDTO request, String userEmail) {
    User user = userRepository.findByEmail(userEmail)

        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


    Post post = Post.builder()
        .cafeName(request.getCafeName())
        .address(request.getAddress())
        .recommend(request.getRecommend())
        .content(request.getContent())
        .isPublic(request.getIsPublic())
        .user(user)
        .build();

    Post savedPost = postRepository.save(post);

    return toPostResponseDTO(savedPost);
  }

  // post 객체를 DTO 로 변환
  private PostResponseDTO toPostResponseDTO(Post post) {
    return PostResponseDTO.builder()
        .id(post.getId())
        .cafeName(post.getCafeName())
        .address(post.getAddress())
        .recommend(post.getRecommend())
        .content(post.getContent())
        .isPublic(post.getIsPublic())
        .userEmail(post.getUser().getEmail())  // 작성자 이메일 추가
        .build();
  }
}